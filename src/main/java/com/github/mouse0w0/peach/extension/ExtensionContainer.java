package com.github.mouse0w0.peach.extension;

import com.google.common.collect.ImmutableList;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ExtensionContainer<T> {

    private final String name;
    private final Class<T> type;
    private final boolean bean;
    private final boolean ordered;

    private final List<ExtensionDescriptor> descriptors = new ArrayList<>();

    private List<T> extensions;

    private ExtensionBeanFactory<?> beanFactory;

    ExtensionContainer(String name, Class<T> type, boolean bean, boolean ordered) {
        this.name = name;
        this.type = type;
        this.bean = bean;
        this.ordered = ordered;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public boolean isBean() {
        return bean;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public List<ExtensionDescriptor> getDescriptors() {
        return descriptors;
    }

    public List<T> getExtensions() {
        if (extensions == null) {
            initExtensions();
        }
        return extensions;
    }

    @SuppressWarnings("unchecked")
    synchronized void initExtensions() {
        if (this.extensions == null) {
            if (ordered) {
                descriptors.sort(Comparator.naturalOrder());
            }
            ImmutableList.Builder<T> builder = ImmutableList.builder();
            for (ExtensionDescriptor descriptor : descriptors) {
                T instance = (T) descriptor.newInstance();
                if (!type.isAssignableFrom(instance.getClass())) {
                    throw new ExtensionException(type + " is not assignable from instance " + instance.getClass());
                }
                builder.add(instance);
            }
            this.extensions = builder.build();
        }
    }

    void register(Element element) {
        if (bean) {
            if (beanFactory == null) {
                beanFactory = new ExtensionBeanFactory<>(type);
            }
            descriptors.add(new ExtensionObjectDescriptor(beanFactory.newInstance(element)));
        } else {
            if (beanFactory == null) {
                beanFactory = new ExtensionBeanFactory<>(ExtensionClassDescriptor.class);
            }
            descriptors.add((ExtensionClassDescriptor) beanFactory.newInstance(element));
        }
    }
}
