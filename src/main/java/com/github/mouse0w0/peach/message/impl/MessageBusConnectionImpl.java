package com.github.mouse0w0.peach.message.impl;

import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.message.MessageBusConnection;
import com.github.mouse0w0.peach.message.Topic;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

final class MessageBusConnectionImpl implements MessageBusConnection, MessageBusImpl.SubscriberHolder {
    private MessageBusImpl messageBus;
    private final AtomicReference<Object[]> subscriptions = new AtomicReference<>();

    public MessageBusConnectionImpl(MessageBusImpl messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public <T> void subscribe(Topic<T> topic, T subscriber) {
        Object[] oldArray;
        Object[] newArray;
        do {
            oldArray = subscriptions.get();
            if (oldArray == null) {
                newArray = new Object[]{topic, subscriber};
            } else {
                int length = oldArray.length;
                newArray = Arrays.copyOf(oldArray, length + 2);
                newArray[length] = topic;
                newArray[length + 1] = subscriber;
            }
        } while (!subscriptions.compareAndSet(oldArray, newArray));
        messageBus.notifySubscription(topic);
    }

    @Override
    public void disconnect() {
        Disposer.dispose(this);
    }

    @Override
    public boolean isDisposed() {
        return messageBus == null;
    }

    @Override
    public <T> void collectSubscribers(Topic<T> topic, List<? super T> result) {
        Object[] array = subscriptions.get();
        for (int i = 0, length = array.length; i < length; i += 2) {
            if (array[i] == topic) {
                result.add((T) array[i + 1]);
            }
        }
    }

    @Override
    public void dispose() {
        MessageBusImpl bus = this.messageBus;
        if (bus == null) {
            return;
        }
        this.messageBus = null;
        bus.notifyConnectionDisposed(this, subscriptions.get());
    }
}
