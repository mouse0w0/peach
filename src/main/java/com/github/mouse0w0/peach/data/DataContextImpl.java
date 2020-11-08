package com.github.mouse0w0.peach.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DataContextImpl extends DataContextBase {

    private final Map<Key<?>, Object> data = new HashMap<>();

    public DataContextImpl() {
    }

    public DataContextImpl(DataContext parent) {
        super(parent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getData(@Nonnull Key<T> key) {
        return (T) data.get(key);
    }

    public <T> void putData(@Nonnull Key<T> key, @Nullable T value) {
        data.put(key, value);
    }
}
