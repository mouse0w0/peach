package com.github.mouse0w0.peach.data;

import javax.annotation.Nonnull;

public interface DataContext {
    Object getData(@Nonnull String key);

    @SuppressWarnings("unchecked")
    default <T> T getData(@Nonnull DataKey<T> key) {
        return (T) getData(key.getName());
    }
}
