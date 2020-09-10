package com.github.mouse0w0.peach.component;

import com.github.mouse0w0.peach.data.DataHolderImpl;
import com.github.mouse0w0.peach.exception.ServiceException;
import com.github.mouse0w0.peach.plugin.ServiceDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class ComponentManagerImpl extends DataHolderImpl implements ComponentManager {
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

    protected void initServices(List<ServiceDescriptor> services) {
        for (ServiceDescriptor service : services) {
            registerService(service);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void registerService(ServiceDescriptor service) {
        Class<?> implementationClass;
        try {
            implementationClass = Class.forName(service.implementationName);
        } catch (ClassNotFoundException e) {
            throw new ServiceException("Not found service implementation class " + service.implementationName, e);
        }
        Class serviceClass;
        try {
            serviceClass = service.interfaceName != null ? Class.forName(service.interfaceName) : implementationClass;
        } catch (ClassNotFoundException e) {
            throw new ServiceException("Not found service interface class " + service.implementationName, e);
        }

        if (service.lazy) {
            registerServiceFactory(serviceClass, () -> newInstance(implementationClass));
        } else {
            registerService(serviceClass, newInstance(implementationClass));
        }
    }

    private <T> T newInstance(Class<T> implementationClass) {
        try {
            return implementationClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ServiceException("Failed to create the service instance of " + implementationClass.getName());
        }
    }
}
