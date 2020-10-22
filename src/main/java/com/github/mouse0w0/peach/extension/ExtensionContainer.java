package com.github.mouse0w0.peach.extension;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public final class ExtensionContainer<T> {

    private final String name;
    private final Class<T> beanClass;
    private final Class<T> interfaceClass;

    private final List<T> extensions = new ArrayList<>();

    private ExtensionBeanFactory<T> beanFactory;

    @SuppressWarnings("unchecked")
    ExtensionContainer(String name, Class<?> beanClass, Class<?> interfaceClass) {
        this.name = name;
        this.beanClass = (Class<T>) beanClass;
        this.interfaceClass = (Class<T>) interfaceClass;
    }

    public String getName() {
        return name;
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public List<T> getExtensions() {
        return extensions;
    }

    @SuppressWarnings("unchecked")
    void register(Element element) {
        if (beanClass != null) {
            if (beanFactory == null) {
                beanFactory = new ExtensionBeanFactory<>(beanClass);
            }
            extensions.add(beanFactory.newInstance(element));
        } else if (interfaceClass != null) {
            org.dom4j.Attribute implementation = element.attribute("implementation");
            try {
                Class<T> implementationClass = (Class<T>) Class.forName(implementation.getValue());
                if (!interfaceClass.isAssignableFrom(implementationClass)) {
                    throw new ExtensionException(implementationClass + " is not implements " + interfaceClass);
                }
                T instance = implementationClass.getConstructor().newInstance();
                extensions.add(instance);
            } catch (ReflectiveOperationException e) {
                throw new ExtensionException("Failed to register extension to " + name, e);
            }
        } else {
            throw new ExtensionException("Failed to register extension to " + name);
        }
    }
}
