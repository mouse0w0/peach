package com.github.mouse0w0.peach.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DataHolderImpl implements DataHolder {
    private Map<Key<?>, Object> data = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getData(@Nonnull Key<T> key) {
        return (T) data.get(key);
    }

    @Override
    public <T> void putData(@Nonnull Key<T> key, @Nullable T value) {
        data.put(key, value);
    }
}
