package com.github.mouse0w0.peach.extension;

import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginException;
import com.google.common.collect.ImmutableList;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class ExtensionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionFactory.class);

    private static final Map<Class<?>, Converter<?>> CONVERTERS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Deserializer> DESERIALIZERS = new ConcurrentHashMap<>();

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final MethodType MT_EMPTY_CONSTRUCTOR = MethodType.methodType(void.class);

    static {
        CONVERTERS.put(String.class, value -> value);
        CONVERTERS.put(boolean.class, Boolean::valueOf);
        CONVERTERS.put(Boolean.class, value -> value != null ? Boolean.valueOf(value) : null);
        CONVERTERS.put(int.class, Integer::valueOf);
        CONVERTERS.put(Integer.class, value -> value != null ? Integer.valueOf(value) : null);
        CONVERTERS.put(long.class, Long::valueOf);
        CONVERTERS.put(Long.class, value -> value != null ? Long.valueOf(value) : null);
        CONVERTERS.put(float.class, Float::valueOf);
        CONVERTERS.put(Float.class, value -> value != null ? Float.valueOf(value) : null);
        CONVERTERS.put(double.class, Double::valueOf);
        CONVERTERS.put(Double.class, value -> value != null ? Double.valueOf(value) : null);
        CONVERTERS.put(BigInteger.class, BigInteger::new);
        CONVERTERS.put(BigDecimal.class, BigDecimal::new);
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Converter<?> getConverter(Field f) {
        Class<?> fieldType = f.getType();
        Converter<?> converter = CONVERTERS.get(fieldType);
        if (converter != null) return converter;

        if (fieldType.isEnum()) {
            CONVERTERS.put(fieldType, converter = value -> Enum.valueOf((Class) fieldType, value.toUpperCase()));
            return converter;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> implementationClass, Plugin plugin, Element element) {
        try {
            Object instance = MethodHandles.privateLookupIn(implementationClass, LOOKUP).findConstructor(implementationClass, MT_EMPTY_CONSTRUCTOR).invoke();
            if (instance instanceof PluginAware) {
                ((PluginAware) instance).setPlugin(plugin);
            }

            if (element != null) {
                Deserializer deserializer = DESERIALIZERS.get(implementationClass);
                if (deserializer == null) {
                    DESERIALIZERS.put(implementationClass, deserializer = new Deserializer(implementationClass));
                }
                deserializer.deserializeInto(instance, element);
            }
            return (T) instance;
        } catch (Throwable e) {
            throw new PluginException("Cannot create extension, implementation=" + implementationClass.getName() + ", plugin=" + plugin.getId() + ")", e);
        }
    }

    private static final class Deserializer {
        private final List<AttributeDeserializer> attributeDeserializers;

        @SuppressWarnings("rawtypes")
        public Deserializer(Class<?> type) throws Throwable {
            MethodHandles.Lookup privateLookup = MethodHandles.privateLookupIn(type, LOOKUP);
            ImmutableList.Builder<AttributeDeserializer> attributeDeserializersBuilder = ImmutableList.builder();
            for (Field f : type.getDeclaredFields()) {
                Attribute attributeAnno = f.getAnnotation(Attribute.class);
                if (attributeAnno != null) {
                    f.setAccessible(true);
                    String attributeName = attributeAnno.value();
                    if (attributeName.isEmpty()) {
                        attributeName = f.getName();
                    }
                    boolean required = f.getAnnotation(Required.class) != null;
                    Class<? extends Converter> converterType = attributeAnno.converter();
                    if (converterType == Converter.class) {
                        Converter<?> converter = getConverter(f);
                        if (converter != null) {
                            attributeDeserializersBuilder.add(new AttributeDeserializer(
                                    attributeName,
                                    required,
                                    converter,
                                    privateLookup.unreflectSetter(f)));
                        } else {
                            LOGGER.error("Not found converter of `{}`, at `{}.{}`", f.getType().getName(), type.getName(), f.getName());
                        }
                    } else {
                        attributeDeserializersBuilder.add(new AttributeDeserializer(
                                attributeName,
                                required,
                                (Converter<?>) MethodHandles.privateLookupIn(converterType, LOOKUP)
                                        .findConstructor(converterType, MT_EMPTY_CONSTRUCTOR)
                                        .invoke(),
                                privateLookup.unreflectSetter(f)));
                    }
                }
            }
            this.attributeDeserializers = attributeDeserializersBuilder.build();
        }

        public void deserializeInto(Object object, Element element) throws Throwable {
            for (AttributeDeserializer deserializer : attributeDeserializers) {
                deserializer.deserializeInto(object, element);
            }
        }
    }

    private static final class AttributeDeserializer {
        private final String name;
        private final boolean required;
        private final Converter<?> converter;
        private final MethodHandle setter;

        public AttributeDeserializer(String name, boolean required, Converter<?> converter, MethodHandle setter) {
            this.name = name;
            this.required = required;
            this.converter = converter;
            this.setter = setter;
        }

        public void deserializeInto(Object object, Element element) throws Throwable {
            String value = element.attributeValue(name);
            if (value != null) {
                setter.invoke(object, converter.convert(value));
            } else if (required) {
                throw new RuntimeException("Missing required attribute value, name=" + name);
            }
        }
    }
}
