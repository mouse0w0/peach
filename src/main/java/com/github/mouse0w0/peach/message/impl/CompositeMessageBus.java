package com.github.mouse0w0.peach.message.impl;

import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.message.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CompositeMessageBus extends MessageBusImpl {
    private final Collection<MessageBusImpl> children = new CopyOnWriteArrayList<>();

    public CompositeMessageBus() {
    }

    public CompositeMessageBus(@NotNull CompositeMessageBus parent) {
        super(parent);
    }

    @Override
    boolean hasChildren() {
        return true;
    }

    @Override
    void invalidateChildren(Topic<?> topic) {
        for (MessageBusImpl child : children) {
            child.subscriberCache.remove(topic);
            child.invalidateChildren(topic);
        }
    }

    void addChild(MessageBusImpl child) {
        if (isDisposed()) {
            throw new IllegalStateException("Already disposed");
        }

        children.add(child);

        for (MessageBusImpl bus = this; bus != null; bus = bus.parent) {
            bus.subscriberCache.clear();
        }
    }

    void removeChild(MessageBusImpl child) {
        if (isDisposed()) return;

        children.remove(child);

        for (MessageBusImpl bus = this; bus != null; bus = bus.parent) {
            bus.subscriberCache.clear();
        }
    }

    @Override
    <T> void collectChildrenSubscribers(Topic<T> topic, List<? super T> result) {
        for (MessageBusImpl child : children) {
            child.collectSubscribers(topic, result);
        }
    }

    @Override
    void disposeChildren() {
        for (MessageBusImpl child : children) {
            Disposer.dispose(child);
        }
    }
}
