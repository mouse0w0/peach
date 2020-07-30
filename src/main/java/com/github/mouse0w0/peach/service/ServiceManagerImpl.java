package com.github.mouse0w0.peach.service;

import com.github.mouse0w0.peach.data.DataHolderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class ServiceManagerImpl extends DataHolderImpl implements ServiceManager {
    private final Map<Class<?>, Object> services = new HashMap<>();
    private final Map<Class<?>, Supplier<?>> serviceFactories = new HashMap<>();

    public <T> void registerService(Class<T> classOfT, T service) {
        services.putIfAbsent(classOfT, service);
    }

    public <T> void registerServiceFactory(Class<T> classOfT, Supplier<T> serviceFactory) {
        serviceFactories.putIfAbsent(classOfT, serviceFactory);
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
