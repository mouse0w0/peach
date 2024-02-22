package com.github.mouse0w0.peach.message;

import com.github.mouse0w0.peach.plugin.ListenerDescriptor;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
final class LazyListenerMessageBusConnection implements MessageBusImpl.SubscriberHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(LazyListenerMessageBusConnection.class);

    private final Map<String, List<ListenerDescriptor>> lazyListenersMap;
    private final Map<Topic<?>, List<?>> listenersMap = new ConcurrentHashMap<>();

    public LazyListenerMessageBusConnection(Map<String, List<ListenerDescriptor>> lazyListenersMap) {
        this.lazyListenersMap = lazyListenersMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void collectSubscribers(Topic<T> topic, List<? super T> result) {
        List<T> listeners = (List<T>) listenersMap.get(topic);
        if (listeners == null) {
            List<ListenerDescriptor> listenerDescriptors = lazyListenersMap.remove(topic.getName());
            if (listenerDescriptors != null) {
                listeners = new ArrayList<>();
                for (ListenerDescriptor listenerDescriptor : listenerDescriptors) {
                    try {
                        listeners.add((T) listenerDescriptor
                                .getPlugin()
                                .getClassLoader()
                                .loadClass(listenerDescriptor.getListenerClassName())
                                .getConstructor()
                                .newInstance());
                    } catch (Throwable e) {
                        LOGGER.error("Cannot create listener, topic={}, listenerClassName={}, plugin={}",
                                listenerDescriptor.getListenerClassName(),
                                listenerDescriptor.getTopic(),
                                listenerDescriptor.getPlugin().getId(),
                                e);
                    }
                }
                listenersMap.put(topic, listeners);
                result.addAll(listeners);
            }
        } else {
            result.addAll(listeners);
        }
    }

    @Override
    public boolean isDisposed() {
        return false;
    }
}
