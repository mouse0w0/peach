package com.github.mouse0w0.peach.data;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

public final class Key<T> {

    private final String name;

    public static <T> Key<T> of(@Nonnull Class<?> clazz) {
        return new Key<>(clazz.getSimpleName());
    }

    public static <T> Key<T> of(@Nonnull String name) {
        return new Key<>(name);
    }

    private Key(@Nonnull String name) {
        this.name = Validate.notNull(name);
    }

    public String getName() {
        return name;
    }

    public T get(DataContext context) {
        return context.getData(this);
    }
}
