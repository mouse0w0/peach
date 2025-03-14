package com.github.mouse0w0.peach.message;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.plugin.ListenerDescriptor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@ApiStatus.Internal
public class MessageBusImpl implements MessageBus {

    interface SubscriberHolder {
        <T> void collectSubscribers(Topic<T> topic, List<? super T> result);

        boolean isDisposed();
    }

    final Map<Topic<?>, Object> publisherCache = new ConcurrentHashMap<>();
    final Map<Topic<?>, Object[]> subscriberCache = new ConcurrentHashMap<>();
    final Collection<SubscriberHolder> subscriberHolders = new ConcurrentLinkedQueue<>();

    CompositeMessageBus parent;

    private Disposable connectionDisposable = Disposer.newDisposable();
    private boolean disposed = false;

    public MessageBusImpl() {
    }

    public MessageBusImpl(@NotNull CompositeMessageBus parent) {
        this.parent = parent;

        parent.addChild(this);
    }

    @Override
    public MessageBus getParent() {
        return parent;
    }

    @Override
    public MessageBusConnection connect() {
        return connect(connectionDisposable);
    }

    @Override
    public MessageBusConnection connect(@NotNull Disposable parentDisposable) {
        MessageBusConnectionImpl connection = new MessageBusConnectionImpl(this);
        Disposer.register(parentDisposable, connection);
        subscriberHolders.add(connection);
        return connection;
    }

    @Override
    public <T> T getPublisher(@NotNull Topic<T> topic) {
        return (T) publisherCache.computeIfAbsent(topic, this::createPublisher);
    }

    public void addLazyListeners(Map<String, List<ListenerDescriptor>> lazyListenersMap) {
        subscriberHolders.add(new LazyListenerMessageBusConnection(lazyListenersMap));
    }

    private <T> Object createPublisher(Topic<T> topic) {
        Class<?> subscriberType = topic.getListenerClass();
        return Proxy.newProxyInstance(subscriberType.getClassLoader(), new Class[]{subscriberType},
                topic.getBroadcastDirection() == BroadcastDirection.TO_PARENT ?
                        new ToParentMessagePublisher<>(this, topic) :
                        new MessagePublisher<>(this, topic));
    }

    <T> Object[] getSubscribers(Topic<T> topic) {
        return subscriberCache.computeIfAbsent(topic, this::computeSubscribers);
    }

    private <T> Object[] computeSubscribers(Topic<T> topic) {
        List<T> result = new ArrayList<>();
        collectSubscribers(topic, result);
        return result.toArray();
    }

    <T> void collectSubscribers(Topic<T> topic, List<? super T> result) {
        for (SubscriberHolder subscriberHolder : subscriberHolders) {
            if (!subscriberHolder.isDisposed()) {
                subscriberHolder.collectSubscribers(topic, result);
            }
        }

        if (hasChildren() && topic.getBroadcastDirection() == BroadcastDirection.TO_CHILDREN) {
            collectChildrenSubscribers(topic, result);
        }
    }

    <T> void collectChildrenSubscribers(Topic<T> topic, List<? super T> result) {
    }

    @Override
    public void dispose() {
        if (disposed) return;
        disposed = true;

        disposeChildren();

        Disposer.dispose(connectionDisposable);

        if (parent != null) {
            parent.removeChild(this);
            parent = null;
        }
    }

    void disposeChildren() {
    }

    boolean isDisposed() {
        return disposed;
    }

    boolean hasChildren() {
        return false;
    }

    void notifySubscription(Topic<?> topic) {
        invalidate(topic);
    }

    void notifyConnectionDisposed(SubscriberHolder subscriberHolder, Object[] subscriptions) {
        if (isDisposed()) return;
        subscriberHolders.remove(subscriberHolder);
        for (int i = 0, length = subscriptions.length; i < length; i += 2) {
            invalidate((Topic<?>) subscriptions[i]);
        }
    }

    void invalidate(Topic<?> topic) {
        subscriberCache.remove(topic);

        BroadcastDirection broadcastDirection = topic.getBroadcastDirection();
        if (broadcastDirection == BroadcastDirection.TO_CHILDREN) {
            for (MessageBusImpl p = parent; p != null; p = p.parent) {
                p.subscriberCache.clear();
            }

            if (hasChildren()) {
                invalidateChildren(topic);
            }
        }
    }

    void invalidateChildren(Topic<?> topic) {
    }

    private static class MessagePublisher<T> implements InvocationHandler {
        private static final Logger LOGGER = LoggerFactory.getLogger(MessagePublisher.class);

        final MessageBusImpl messageBus;
        final Topic<T> topic;

        public MessagePublisher(MessageBusImpl messageBus, Topic<T> topic) {
            this.messageBus = messageBus;
            this.topic = topic;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return handleObjectMethod(proxy, method, args);
            } else {
                if (messageBus.isDisposed()) {
                    throw new IllegalStateException("Already disposed");
                }

                if (args == null) {
                    publishWithoutArgs(MethodHandleCache.get(method, args));
                } else {
                    publish(MethodHandleCache.get(method, args), args);
                }
                return null;
            }
        }

        Object handleObjectMethod(Object proxy, Method method, Object[] args) {
            switch (method.getName()) {
                case "toString":
                    return "Publisher(" + topic + ")";
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "equals":
                    return proxy == args[0] ? Boolean.TRUE : Boolean.FALSE;
                default:
                    LOGGER.error("Unsupported Object's method invoked for publisher: {}", method.getName());
                    return null;
            }
        }

        void publish(MethodHandle handle, Object[] args) throws Throwable {
            for (Object subscriber : messageBus.getSubscribers(topic)) {
                handle.bindTo(subscriber).invokeExact(args);
            }
        }

        void publishWithoutArgs(MethodHandle handle) throws Throwable {
            for (Object subscriber : messageBus.getSubscribers(topic)) {
                handle.invoke(subscriber);
            }
        }
    }

    private static final class ToParentMessagePublisher<T> extends MessagePublisher<T> {
        public ToParentMessagePublisher(MessageBusImpl messageBus, Topic<T> topic) {
            super(messageBus, topic);
        }

        @Override
        void publish(MethodHandle handle, Object[] args) throws Throwable {
            for (MessageBusImpl bus = messageBus; bus != null; bus = bus.parent) {
                for (Object subscriber : bus.getSubscribers(topic)) {
                    handle.bindTo(subscriber).invokeExact(args);
                }
            }
        }

        @Override
        void publishWithoutArgs(MethodHandle handle) throws Throwable {
            for (MessageBusImpl bus = messageBus; bus != null; bus = bus.parent) {
                for (Object subscriber : bus.getSubscribers(topic)) {
                    handle.invoke(subscriber);
                }
            }
        }
    }
}
