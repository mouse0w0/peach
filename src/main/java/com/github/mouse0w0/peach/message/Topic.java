package com.github.mouse0w0.peach.message;

import com.github.mouse0w0.peach.util.Validate;

import javax.annotation.Nonnull;

public final class Topic<T> {
    private final String name;
    private final Class<T> listenerClass;
    private final BroadcastDirection broadcastDirection;

    public Topic(@Nonnull Class<T> listenerClass) {
        this(listenerClass.getName(), listenerClass, BroadcastDirection.TO_CHILDREN);
    }

    public Topic(@Nonnull Class<T> listenerClass, @Nonnull BroadcastDirection broadcastDirection) {
        this(listenerClass.getName(), listenerClass, broadcastDirection);
    }

    public Topic(@Nonnull String name, @Nonnull Class<T> listenerClass) {
        this(name, listenerClass, BroadcastDirection.TO_CHILDREN);
    }

    public Topic(@Nonnull String name, @Nonnull Class<T> listenerClass, @Nonnull BroadcastDirection broadcastDirection) {
        this.name = Validate.notEmpty(name);
        this.listenerClass = Validate.notNull(listenerClass);
        this.broadcastDirection = Validate.notNull(broadcastDirection);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Class<T> getListenerClass() {
        return listenerClass;
    }

    @Nonnull
    public BroadcastDirection getBroadcastDirection() {
        return broadcastDirection;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                ", listenerClass=" + listenerClass +
                ", broadcastDirection=" + broadcastDirection +
                '}';
    }
}
