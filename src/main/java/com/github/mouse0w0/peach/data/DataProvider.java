package com.github.mouse0w0.peach.data;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface DataProvider {

    static DataProvider create(@Nonnull final DataKey<?> dataKey, final Object data) {
        final String key = dataKey.getName();
        return k -> key.equals(k) ? data : null;
    }

    static DataProvider create(@Nonnull final String key, final Object data) {
        return k -> key.equals(k) ? data : null;
    }

    Object getData(@Nonnull String key);
}
