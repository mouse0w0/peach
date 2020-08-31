package com.github.mouse0w0.peach.component;

import com.github.mouse0w0.peach.data.DataHolderImpl;
import com.github.mouse0w0.peach.exception.ServiceException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void registerServices(URL url) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(url);
            Element services = document.getRootElement();
            for (Element service : services.elements("service")) {
                String implementationName = service.attributeValue("implementation");
                String interfaceName = service.attributeValue("interface");
                boolean lazy = "true".equals(service.attributeValue("lazy"));

                Class<?> implementationClass = Class.forName(implementationName);
                Class serviceClass = interfaceName != null ? Class.forName(interfaceName) : implementationClass;
                if (lazy) {
                    Supplier<?> serviceFactory = () -> {
                        try {
                            return implementationClass.newInstance();
                        } catch (ReflectiveOperationException e) {
                            throw new ServiceException("Cannot initialize service class " + implementationClass.getName());
                        }
                    };
                    registerServiceFactory(serviceClass, serviceFactory);
                } else {
                    registerService(serviceClass, implementationClass.newInstance());
                }
            }
        } catch (DocumentException | ReflectiveOperationException e) {
            throw new ServiceException("Cannot load services: " + url, e);
        }
    }
}
