package com.github.mouse0w0.peach.service;

import java.util.function.Supplier;

public interface ServiceManager {

    <T> void registerService(Class<T> classOfT, T service);

    <T> void registerService(Class<T> classOfT, Supplier<T> serviceFactory, boolean preload);

    default <T> T getService(Class<T> classOfT) {
        return getService(classOfT, true);
    }

    <T> T getService(Class<T> classOfT, boolean createIfNeeded);
}
