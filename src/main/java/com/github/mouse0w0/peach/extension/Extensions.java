package com.github.mouse0w0.peach.extension;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class Extensions {

    private static final Logger LOGGER = LoggerFactory.getLogger("Extension");

    private static final Map<String, ExtensionContainer<?>> CONTAINERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> ExtensionContainer<T> getExtensionContainer(String name) {
        return (ExtensionContainer<T>) CONTAINERS.get(name);
    }

    public static void registerExtensionPoints(Element extensionPoints) {
        for (Element extensionPoint : extensionPoints.elements()) {
            String name = extensionPoint.attributeValue("name");
            if (name == null || name.isEmpty()) {
                throw new ExtensionException("Attribute \"name\" cannot be null");
            }

            if (CONTAINERS.containsKey(name)) {
                throw new ExtensionException("Extension point \"" + name + "\" has been used");
            }

            String beanName = extensionPoint.attributeValue("beanClass");
            String interfaceName = extensionPoint.attributeValue("interface");

            if ((beanName == null && interfaceName == null) ||
                    (beanName != null && interfaceName != null)) {
                throw new ExtensionException("Failed to register extension point \"" + name + "\" ," +
                        " \"beanClass\" and \"interface\" cannot be both null or both non null");
            }

            Class<?> type;
            boolean beanMode;
            if (beanName != null) {
                try {
                    type = Class.forName(beanName);
                    beanMode = true;
                } catch (ClassNotFoundException e) {
                    throw new ExtensionException("Not found bean class " + beanName);
                }
            } else {
                try {
                    type = Class.forName(interfaceName);
                    beanMode = false;
                } catch (ClassNotFoundException e) {
                    throw new ExtensionException("Not found interface class " + interfaceName);
                }
            }

            boolean ordered = "true".equals(extensionPoint.attributeValue("ordered", beanMode ? "false" : "true"));

            ExtensionContainer<?> container = new ExtensionContainer<>(name, type, beanMode, ordered);
            CONTAINERS.put(name, container);
        }
    }

    public static void registerExtensions(Element extensions) {
        for (Element extension : extensions.elements()) {
            ExtensionContainer<?> container = CONTAINERS.get(extension.getName());
            if (container == null) {
                LOGGER.warn("Failed to register extension {}, the extension point may not be declared.", extension.getNamespace());
                continue;
            }
            container.register(extension);
        }
    }
}
