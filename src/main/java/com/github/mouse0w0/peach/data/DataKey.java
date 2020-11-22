package com.github.mouse0w0.peach.data;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class DataKey<T> {

    @SuppressWarnings("rawtypes")
    private static final ConcurrentMap<String, DataKey> dataKeys = new ConcurrentHashMap<>();

    private final String name;

    @SuppressWarnings("unchecked")
    public static <T> DataKey<T> create(@Nonnull String name) {
        return dataKeys.computeIfAbsent(name, DataKey::new);
    }

    private DataKey(@Nonnull String name) {
        this.name = Validate.notNull(name);
    }

    public String getName() {
        return name;
    }

    public boolean is(String key) {
        return name.equals(key);
    }

    @SuppressWarnings("unchecked")
    public T get(DataContext context) {
        return (T) context.getData(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataKey<?> dataKey = (DataKey<?>) o;
        return name.equals(dataKey.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
