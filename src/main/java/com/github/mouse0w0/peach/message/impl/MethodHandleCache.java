package com.github.mouse0w0.peach.message.impl;

import com.google.common.collect.MapMaker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Map;

final class MethodHandleCache {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final Map<Method, MethodHandle> CACHE = new MapMaker().weakKeys().concurrencyLevel(1).makeMap();

    static MethodHandle get(Method method, Object[] args) {
        return CACHE.computeIfAbsent(method, m -> {
            m.setAccessible(true);
            try {
                MethodHandle result = LOOKUP.unreflect(m);
                return args == null ? result : result.asSpreader(Object[].class, args.length);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
