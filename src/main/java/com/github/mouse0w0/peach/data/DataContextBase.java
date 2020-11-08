package com.github.mouse0w0.peach.data;

import javax.annotation.Nonnull;

public abstract class DataContextBase implements DataContext {
    protected final DataContext parent;

    public DataContextBase() {
        this(null);
    }

    public DataContextBase(DataContext parent) {
        this.parent = parent;
    }

    @Override
    public <T> T getData(@Nonnull Key<T> key) {
        return parent != null ? parent.getData(key) : null;
    }
}
