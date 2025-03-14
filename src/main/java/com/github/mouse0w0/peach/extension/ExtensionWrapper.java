package com.github.mouse0w0.peach.extension;

import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginException;
import org.dom4j.Element;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

final class ExtensionWrapper<T> {
    private static final VarHandle EXTENSION;

    static {
        try {
            EXTENSION = MethodHandles.lookup().findVarHandle(ExtensionWrapper.class, "extension", Object.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Object implementationClassOrName;
    private final Plugin plugin;
    private final String id;
    private final ExtensionOrder order;
    private Element element;

    private volatile T extension;
    private boolean initializing;

    public ExtensionWrapper(String implementationClassOrName, Plugin plugin, String id, ExtensionOrder order, Element element) {
        this.implementationClassOrName = implementationClassOrName;
        this.plugin = plugin;
        this.id = id;
        this.order = order;
        this.element = element;
    }

    public ExtensionWrapper(Class<T> implementationClassOrName, Plugin plugin, String id, ExtensionOrder order, Element element) {
        this.implementationClassOrName = implementationClassOrName;
        this.plugin = plugin;
        this.id = id;
        this.order = order;
        this.element = element;
    }

    @SuppressWarnings("unchecked")
    public T getExtension() {
        T extension = (T) EXTENSION.getAcquire(this);
        if (extension != null) return extension;

        synchronized (this) {
            extension = (T) EXTENSION.getAcquire(this);
            if (extension != null) return extension;

            if (initializing)
                throw new PluginException("Cyclic extension initialization" +
                        ", implementation=" + getImplementation() +
                        ", plugin=" + plugin.getId());

            try {
                initializing = true;
                Class<T> implementationClass;
                if (implementationClassOrName instanceof String) {
                    try {
                        implementationClass = (Class<T>) plugin.getClassLoader().loadClass((String) implementationClassOrName);
                    } catch (ClassNotFoundException e) {
                        throw new PluginException("Cannot found extension implementation class" +
                                ", implementation=" + implementationClassOrName +
                                ", plugin=" + plugin.getId());
                    }
                } else {
                    implementationClass = (Class<T>) implementationClassOrName;
                }
                extension = ExtensionFactory.create(implementationClass, plugin, element);
                EXTENSION.setRelease(this, extension);
                element = null;
            } finally {
                initializing = false;
            }
        }
        return extension;
    }

    public String getImplementation() {
        return implementationClassOrName instanceof String ?
                (String) implementationClassOrName :
                ((Class<?>) implementationClassOrName).getName();
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getId() {
        return id;
    }

    public ExtensionOrder getOrder() {
        return order;
    }
}
