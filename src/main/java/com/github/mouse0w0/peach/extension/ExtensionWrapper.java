package com.github.mouse0w0.peach.extension;

import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginException;
import org.dom4j.Element;

final class ExtensionWrapper<T> {
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
        T extension = this.extension;
        if (extension != null) return extension;

        synchronized (this) {
            extension = this.extension;
            if (extension != null) return extension;

            if (initializing)
                throw new PluginException("Cyclic extension initialization"
                        + ", implementation=" + getImplementation()
                        + ", plugin=" + plugin.getId());

            try {
                initializing = true;
                Class<T> implementationClass;
                if (implementationClassOrName instanceof String) {
                    implementationClass = (Class<T>) plugin.getClassLoader().loadClass((String) implementationClassOrName);
                } else {
                    implementationClass = (Class<T>) implementationClassOrName;
                }
                this.extension = extension = ExtensionFactory.create(implementationClass, plugin, element);
                element = null;
            } catch (Throwable e) {
                throw new PluginException("Cannot create extension"
                        + ", implementation=" + getImplementation()
                        + ", plugin=" + plugin.getId(), e);
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
