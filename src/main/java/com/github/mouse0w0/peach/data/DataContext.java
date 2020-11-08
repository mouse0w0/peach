package com.github.mouse0w0.peach.data;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface DataContext {
    <T> T getData(@Nonnull Key<T> key);

    default <T> T getData(@Nonnull Key<T> key, T defaultValue) {
        T data = getData(key);
        return data != null ? data : defaultValue;
    }

    default <T> T getData(@Nonnull Key<T> key, @Nonnull Supplier<T> defaultSupplier) {
        T data = getData(key);
        return data != null ? data : defaultSupplier.get();
    }
}
