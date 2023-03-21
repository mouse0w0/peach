package com.github.mouse0w0.peach.data;

import org.jetbrains.annotations.NotNull;

public interface DataContext {
    Object getData(@NotNull String key);

    @SuppressWarnings("unchecked")
    default <T> T getData(@NotNull DataKey<T> key) {
        return (T) getData(key.getName());
    }
}
