package com.github.mouse0w0.peach.data;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DataProvider {

    static DataProvider create(@NotNull final DataKey<?> dataKey, final Object data) {
        final String key = dataKey.getName();
        return k -> key.equals(k) ? data : null;
    }

    static DataProvider create(@NotNull final String key, final Object data) {
        return k -> key.equals(k) ? data : null;
    }

    Object getData(@NotNull String key);
}
