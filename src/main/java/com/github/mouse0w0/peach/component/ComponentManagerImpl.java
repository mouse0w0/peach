package com.github.mouse0w0.peach.component;

import com.github.mouse0w0.peach.data.DataHolderImpl;
import com.github.mouse0w0.peach.exception.ServiceException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
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


    protected void registerServices(URL url) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(url);
            Element services = document.getRootElement();
            registerServices(services.elements("service"));
        } catch (DocumentException e) {
            throw new ServiceException("Cannot load services from " + url, e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void registerServices(Collection<Element> services) {
        for (Element service : services) {
            String implementationName = service.attributeValue("implementation");
            String interfaceName = service.attributeValue("interface");
            boolean lazy = "true".equals(service.attributeValue("lazy"));

            Class<?> implementationClass;
            try {
                implementationClass = Class.forName(implementationName);
            } catch (ClassNotFoundException e) {
                throw new ServiceException("Not found service implementation class " + implementationName, e);
            }
            Class serviceClass;
            try {
                serviceClass = interfaceName != null ? Class.forName(interfaceName) : implementationClass;
            } catch (ClassNotFoundException e) {
                throw new ServiceException("Not found service interface class " + implementationName, e);
            }

            if (lazy) {
                registerServiceFactory(serviceClass, () -> newInstance(implementationClass));
            } else {
                registerService(serviceClass, newInstance(implementationClass));
            }
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
