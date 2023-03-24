package com.github.mouse0w0.peach.util;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.Function;

public final class LazyInitializer<T, R> {
    private final VarHandle varHandle;
    private final Function<T, R> factory;

    public LazyInitializer(Class<T> classOfT, String name, Class<R> classOfR, Function<T, R> factory) {
        try {
            this.varHandle = MethodHandles.privateLookupIn(classOfT, MethodHandles.lookup()).findVarHandle(classOfT, name, classOfR);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot find VarHandle", e);
        }
        this.factory = Validate.notNull(factory);
    }

    public LazyInitializer(VarHandle varHandle, Function<T, R> factory) {
        this.varHandle = Validate.notNull(varHandle);
        this.factory = Validate.notNull(factory);
    }

    @SuppressWarnings("unchecked")
    public R get(T t) {
        R result = (R) varHandle.getVolatile(t);
        if (result != null) return result;

        synchronized (t) {
            result = (R) varHandle.getVolatile(t);
            if (result != null) return result;

            result = factory.apply(t);
            varHandle.setVolatile(t, result);
            return result;
        }
    }
}
