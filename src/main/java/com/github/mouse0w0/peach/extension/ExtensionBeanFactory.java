package com.github.mouse0w0.peach.extension;

import org.apache.commons.lang3.Validate;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class ExtensionBeanFactory<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger("Extension");

    private final static Map<Class<?>, AttributeConverter> CONVERTERS;

    static {
        CONVERTERS = new HashMap<>();
        CONVERTERS.put(String.class, value -> value);
        CONVERTERS.put(boolean.class, "true"::equals);
        CONVERTERS.put(int.class, Integer::parseInt);
        CONVERTERS.put(long.class, Long::parseLong);
        CONVERTERS.put(float.class, Float::parseFloat);
        CONVERTERS.put(double.class, Double::parseDouble);
        CONVERTERS.put(Class.class, value -> {
            try {
                return Class.forName(value, false, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException ignored) {
                return null;
            }
        });
    }

    private final Class<T> beanClass;

    private final Constructor<T> constructor;
    private final Map<String, AttributeDescriptor> attributes = new HashMap<>();

    public ExtensionBeanFactory(Class<T> beanClass) {
        this.beanClass = Validate.notNull(beanClass);

        try {
            constructor = beanClass.getConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Not found the non-parameter constructor of " + beanClass, e);
        }

        for (Field field : beanClass.getDeclaredFields()) {
            Attribute attribute = field.getAnnotation(Attribute.class);
            if (attribute == null) continue;
            String attributeName = attribute.value();
            field.setAccessible(true);
            AttributeDescriptor descriptor = new AttributeDescriptor(attributeName, field, CONVERTERS.get(field.getType()));
            attributes.put(attributeName, descriptor);
        }
    }

    public T newInstance(Element element) {
        try {
            T bean = constructor.newInstance();
            for (org.dom4j.Attribute attribute : element.attributes()) {
                String name = attribute.getName();

                AttributeDescriptor descriptor = attributes.get(name);
                if (descriptor == null) {
                    LOGGER.warn("Not found the \"{}\" attribute of {}.", name, beanClass);
                    continue;
                }

                descriptor.set(bean, attribute);
            }
            return bean;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to new instance of " + beanClass, e);
        }
    }

    private static class AttributeDescriptor {
        private final String name;
        private final Field field;
        private final AttributeConverter converter;

        public AttributeDescriptor(String name, Field field, AttributeConverter converter) {
            this.name = name;
            this.field = field;
            this.converter = converter;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private void set(Object bean, org.dom4j.Attribute attribute) throws ReflectiveOperationException {
            String value = attribute.getValue();

            if (converter == null) {
                Class<?> fieldType = field.getType();
                if (Enum.class.isAssignableFrom(fieldType)) {
                    field.set(bean, Enum.valueOf((Class) fieldType, value));
                    return;
                }

                Class<?> clazz = Class.forName(value);
                if (fieldType.isAssignableFrom(clazz)) {
                    field.set(bean, clazz.getConstructor().newInstance());
                } else {
                    throw new UnsupportedOperationException("Cannot set the \"" + name + "\" attribute of " + bean.getClass());
                }
            } else {
                field.set(bean, converter.convert(value));
            }
        }
    }
}
