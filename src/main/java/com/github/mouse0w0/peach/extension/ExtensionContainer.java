package com.github.mouse0w0.peach.extension;

import com.google.common.collect.ImmutableList;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ExtensionContainer<T> {

    private final String name;
    private final Class<T> type;
    private final boolean beanMode;
    private final boolean ordered;

    private final List<ExtensionDescriptor> descriptors = new ArrayList<>();

    private List<T> extensions;

    private ExtensionBeanFactory<?> beanFactory;

    @SuppressWarnings("unchecked")
    ExtensionContainer(String name, Class<T> type, boolean beanMode, boolean ordered) {
        this.name = name;
        this.type = type;
        this.beanMode = beanMode;
        this.ordered = ordered;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public boolean isBeanMode() {
        return beanMode;
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
            List<T> extensions = new ArrayList<>(descriptors.size());
            if (ordered) {
                descriptors.sort(Comparator.naturalOrder());
            }
            for (ExtensionDescriptor descriptor : descriptors) {
                T instance = (T) descriptor.newInstance();
                if (!type.isAssignableFrom(instance.getClass())) {
                    throw new ExtensionException(type + " is not assignable from instance " + instance.getClass());
                }
                extensions.add(instance);
            }
            this.extensions = ImmutableList.copyOf(extensions);
        }
    }

    @SuppressWarnings("unchecked")
    void register(Element element) {
        if (beanMode) {
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
