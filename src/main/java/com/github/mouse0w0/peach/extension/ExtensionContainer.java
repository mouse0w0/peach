package com.github.mouse0w0.peach.extension;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class ExtensionContainer<T> {

    private final String name;
    private final Class<T> beanClass;
    private final Class<T> interfaceClass;

    private List<T> extensions;

    private ExtensionBeanFactory<?> beanFactory;

    private List<ExtensionImplBean> implBeans;

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
        if (extensions == null) {
            extensions = loadExtensions();
        }
        return extensions;
    }

    @SuppressWarnings("unchecked")
    List<T> loadExtensions() {
        if (implBeans == null) return Collections.emptyList();

        List<T> extensions = new ArrayList<>();
        implBeans.sort(Comparator.comparing(o -> o.order));
        for (ExtensionImplBean bean : implBeans) {
            try {
                Class<?> implementationClass = Class.forName(bean.implementation);
                if (!interfaceClass.isAssignableFrom(implementationClass)) {
                    throw new ExtensionException(implementationClass + " is not implements " + interfaceClass);
                }
                T instance = (T) implementationClass.getConstructor().newInstance();
                extensions.add(instance);
            } catch (ReflectiveOperationException e) {
                throw new ExtensionException("Failed to load extension \"" + bean.implementation + "\" to " + name, e);
            }
        }
        return extensions;
    }

    @SuppressWarnings("unchecked")
    void register(Element element) {
        if (beanClass != null) {
            if (beanFactory == null) {
                extensions = new ArrayList<>();
                beanFactory = new ExtensionBeanFactory<>(beanClass);
            }
            extensions.add((T) beanFactory.newInstance(element));
        } else if (interfaceClass != null) {
            if (beanFactory == null) {
                implBeans = new ArrayList<>();
                beanFactory = new ExtensionBeanFactory<>(ExtensionImplBean.class);
            }
            implBeans.add((ExtensionImplBean) beanFactory.newInstance(element));
        } else {
            throw new ExtensionException("Failed to register extension to " + name);
        }
    }
}
