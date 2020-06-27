package com.github.mouse0w0.peach.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class ServiceManagerImpl implements ServiceManager {
    private final Map<Class<?>, Object> services = new HashMap<>();
    private final Map<Class<?>, Supplier<?>> serviceFactories = new HashMap<>();

    @Override
    public <T> void registerService(Class<T> classOfT, T service) {
        services.putIfAbsent(classOfT, service);
    }

    @Override
    public <T> void registerService(Class<T> classOfT, Supplier<T> serviceFactory, boolean preload) {
        if (preload) registerService(classOfT, serviceFactory.get());
        else serviceFactories.putIfAbsent(classOfT, serviceFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> classOfT, boolean createIfNeeded) {
        return (T) (createIfNeeded ? services.computeIfAbsent(classOfT, key -> {
            Supplier<?> serviceFactory = serviceFactories.get(key);
            return serviceFactory != null ? serviceFactory.get() : null;
        }) : services.get(classOfT));
    }
}
