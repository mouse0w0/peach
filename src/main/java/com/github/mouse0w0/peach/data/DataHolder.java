package com.github.mouse0w0.peach.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public interface DataHolder {

    <T> T getData(@Nonnull Key<T> key);

    default <T> T getData(@Nonnull Key<T> key, T defaultValue) {
        T data = getData(key);
        return data != null ? data : defaultValue;
    }

    default <T> T getData(@Nonnull Key<T> key, @Nonnull Supplier<T> defaultSupplier) {
        T data = getData(key);
        return data != null ? data : defaultSupplier.get();
    }

    <T> void putData(@Nonnull Key<T> key, @Nullable T value);
}
