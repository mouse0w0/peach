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

    private final static Map<Class<?>, AttributeSetter> attributeSetters;

    static {
        attributeSetters = new HashMap<>();
        attributeSetters.put(String.class, (bean, field, value) -> field.set(bean, value));
        attributeSetters.put(Class.class, (bean, field, value) -> field.set(bean, Class.forName(value)));
        attributeSetters.put(boolean.class, (bean, field, value) -> field.setBoolean(bean, "true".equals(value)));
        attributeSetters.put(int.class, (bean, field, value) -> field.setInt(bean, Integer.parseInt(value)));
        attributeSetters.put(float.class, (bean, field, value) -> field.setFloat(bean, Float.parseFloat(value)));
        attributeSetters.put(double.class, (bean, field, value) -> field.setDouble(bean, Double.parseDouble(value)));
    }

    private final Class<T> beanClass;

    private final Constructor<T> constructor;
    private final Map<String, Field> attributes = new HashMap<>();

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

            field.setAccessible(true);
            attributes.put(attribute.value(), field);
        }
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public T newInstance(Element element) {
        try {
            T bean = constructor.newInstance();
            for (org.dom4j.Attribute attribute : element.attributes()) {
                String name = attribute.getName();
                String value = attribute.getValue();

                Field field = attributes.get(name);
                if (field == null) {
                    LOGGER.warn("Cannot set the \"{}\" attribute of {}.", name, beanClass);
                    continue;
                }

                AttributeSetter setter = attributeSetters.get(field.getType());
                if (setter == null) {
                    Class<?> clazz = Class.forName(value);
                    if (field.getType().isAssignableFrom(clazz)) {
                        field.set(bean, clazz.getConstructor().newInstance());
                    } else {
                        LOGGER.warn("Cannot set the \"{}\" attribute of {}.", name, beanClass);
                    }
                    continue;
                }

                setter.set(bean, field, value);
            }
            return bean;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to new instance of " + beanClass, e);
        }
    }

    public interface AttributeSetter {
        void set(Object bean, Field field, String value) throws ReflectiveOperationException;
    }
}
