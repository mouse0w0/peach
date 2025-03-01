package com.github.mouse0w0.peach.message;

import com.github.mouse0w0.peach.util.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Topic<T> {
    private final String name;
    private final Class<T> listenerClass;
    private final BroadcastDirection broadcastDirection;

    public Topic(@NotNull Class<T> listenerClass, @NotNull BroadcastDirection broadcastDirection) {
        this(listenerClass.getName(), listenerClass, broadcastDirection);
    }

    public Topic(@NotNull String name, @NotNull Class<T> listenerClass, @NotNull BroadcastDirection broadcastDirection) {
        this.name = Validate.notEmpty(name);
        this.listenerClass = Objects.requireNonNull(listenerClass);
        this.broadcastDirection = Objects.requireNonNull(broadcastDirection);
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Class<T> getListenerClass() {
        return listenerClass;
    }

    @NotNull
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
