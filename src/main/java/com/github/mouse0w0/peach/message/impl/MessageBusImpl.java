package com.github.mouse0w0.peach.message.impl;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.MessageBus;
import com.github.mouse0w0.peach.message.MessageBusConnection;
import com.github.mouse0w0.peach.message.Topic;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageBusImpl implements MessageBus {

    interface SubscriberHolder {
        <T> void collectSubscribers(Topic<T> topic, List<? super T> result);

        boolean isDisposed();
    }

    final Map<Topic<?>, Object> publisherCache = new ConcurrentHashMap<>();
    final Map<Topic<?>, Object[]> subscriberCache = new ConcurrentHashMap<>();
    final Collection<SubscriberHolder> subscriberHolders = ConcurrentHashMap.newKeySet();

    CompositeMessageBus parent;

    private Disposable connectionDisposable = Disposer.newDisposable();
    private boolean disposed = false;

    MessageBusImpl() {
    }

    MessageBusImpl(@Nonnull CompositeMessageBus parent) {
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
    public MessageBusConnection connect(@Nonnull Disposable parentDisposable) {
        MessageBusConnectionImpl connection = new MessageBusConnectionImpl(this);
        Disposer.register(parentDisposable, connection);
        subscriberHolders.add(connection);
        return connection;
    }

    @Override
    public <T> T getPublisher(@Nonnull Topic<T> topic) {
        return (T) publisherCache.computeIfAbsent(topic, this::createPublisher);
    }

    private <T> Object createPublisher(Topic<T> topic) {
        Class<?> subscriberType = topic.getType();
        return Proxy.newProxyInstance(subscriberType.getClassLoader(), new Class[]{subscriberType},
                topic.getBroadcastDirection() == BroadcastDirection.TO_PARENT ?
                        new ToParentMessagePublisher<>(this, topic) :
                        new MessagePublisher<>(this, topic));
    }

    <T> Object[] getSubscribers(Topic<T> topic) {
        return subscriberCache.computeIfAbsent(topic, this::computeSubscribers);
    }

    private <T> Object[] computeSubscribers(Topic<T> topic) {
        List<T> result = new ReferenceArrayList<>();
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

        if (connectionDisposable != null) {
            Disposer.dispose(connectionDisposable);
            connectionDisposable = null;
        }

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
                    return publishWithoutArgs(MethodHandleCache.get(method, args));
                } else {
                    return publish(MethodHandleCache.get(method, args), args);
                }
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
                    LOGGER.error("Unsupported Object's method invoked for publisher: " + method.getName());
                    return null;
            }
        }

        Object publish(MethodHandle handle, Object[] args) throws Throwable {
            for (Object subscriber : messageBus.getSubscribers(topic)) {
                handle.bindTo(subscriber).invokeExact(args);
            }
            return null;
        }

        Object publishWithoutArgs(MethodHandle handle) throws Throwable {
            for (Object subscriber : messageBus.getSubscribers(topic)) {
                handle.invoke(subscriber);
            }
            return null;
        }
    }

    private static final class ToParentMessagePublisher<T> extends MessagePublisher<T> {
        public ToParentMessagePublisher(MessageBusImpl messageBus, Topic<T> topic) {
            super(messageBus, topic);
        }

        @Override
        Object publish(MethodHandle handle, Object[] args) throws Throwable {
            for (MessageBusImpl bus = messageBus; bus != null; bus = bus.parent) {
                for (Object subscriber : bus.getSubscribers(topic)) {
                    handle.bindTo(subscriber).invokeExact(args);
                }
            }
            return null;
        }

        @Override
        Object publishWithoutArgs(MethodHandle handle) throws Throwable {
            for (MessageBusImpl bus = messageBus; bus != null; bus = bus.parent) {
                for (Object subscriber : bus.getSubscribers(topic)) {
                    handle.invoke(subscriber);
                }
            }
            return null;
        }
    }
}
