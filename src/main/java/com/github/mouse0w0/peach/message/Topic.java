package com.github.mouse0w0.peach.message;

import com.github.mouse0w0.peach.util.Validate;

import javax.annotation.Nonnull;

public final class Topic<T> {
    private final String name;
    private final Class<T> type;
    private final BroadcastDirection broadcastDirection;

    public Topic(@Nonnull Class<T> type) {
        this(type.getSimpleName(), type, BroadcastDirection.TO_CHILDREN);
    }

    public Topic(@Nonnull Class<T> type, @Nonnull BroadcastDirection broadcastDirection) {
        this(type.getSimpleName(), type, broadcastDirection);
    }

    public Topic(@Nonnull String name, @Nonnull Class<T> type) {
        this(name, type, BroadcastDirection.TO_CHILDREN);
    }

    public Topic(@Nonnull String name, @Nonnull Class<T> type, @Nonnull BroadcastDirection broadcastDirection) {
        this.name = Validate.notEmpty(name);
        this.type = Validate.notNull(type);
        this.broadcastDirection = Validate.notNull(broadcastDirection);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Class<T> getType() {
        return type;
    }

    @Nonnull
    public BroadcastDirection getBroadcastDirection() {
        return broadcastDirection;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", broadcastDirection=" + broadcastDirection +
                '}';
    }
}
