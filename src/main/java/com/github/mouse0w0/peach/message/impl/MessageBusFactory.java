package com.github.mouse0w0.peach.message.impl;

import com.github.mouse0w0.peach.message.MessageBus;

public final class MessageBusFactory {
    public static MessageBus create(MessageBus parent) {
        if (parent == null) {
            return new CompositeMessageBus();
        } else if (parent.getParent() == null) {
            return new CompositeMessageBus((CompositeMessageBus) parent);
        } else {
            return new MessageBusImpl((CompositeMessageBus) parent);
        }
    }

    private MessageBusFactory() {
    }
}
