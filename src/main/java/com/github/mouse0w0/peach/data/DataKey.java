package com.github.mouse0w0.peach.data;

import com.github.mouse0w0.peach.action.ActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class DataKey<T> {

    private static final ConcurrentMap<String, DataKey<?>> DATA_KEYS = new ConcurrentHashMap<>();

    private final String name;

    @SuppressWarnings("unchecked")
    public static <T> DataKey<T> create(@NotNull String name) {
        return (DataKey<T>) DATA_KEYS.computeIfAbsent(name, DataKey::new);
    }

    private DataKey(@NotNull String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    public boolean is(String key) {
        return name.equals(key);
    }

    @SuppressWarnings("unchecked")
    public T get(ActionEvent event) {
        return (T) event.getData(name);
    }

    @SuppressWarnings("unchecked")
    public T get(DataContext context) {
        return (T) context.getData(name);
    }

    @SuppressWarnings("unchecked")
    public T get(DataProvider provider) {
        return (T) provider.getData(name);
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
