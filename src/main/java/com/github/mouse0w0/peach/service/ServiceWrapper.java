package com.github.mouse0w0.peach.service;

import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginException;

final class ServiceWrapper<T> {
    private final Plugin plugin;
    private final ServiceDescriptor descriptor;

    private volatile T service;
    private boolean initializing;

    public ServiceWrapper(Plugin plugin, ServiceDescriptor descriptor) {
        this.plugin = plugin;
        this.descriptor = descriptor;
    }

    public T getService(ServiceManagerImpl serviceManager, boolean createIfNeeded) {
        T service = this.service;
        if (service != null) return service;

        synchronized (this) {
            service = this.service;
            if (service != null) return service;

            if (!createIfNeeded) return null;

            if (initializing)
                throw new PluginException("Cyclic service initialization"
                        + ", interface=" + descriptor.getInterfaceClassName()
                        + ", implementation=" + descriptor.getImplementationClassName()
                        + ", plugin=" + plugin.getId());

            try {
                initializing = true;
                this.service = service = serviceManager.newServiceInstance(plugin.getClassLoader().loadClass(descriptor.getImplementationClassName()), plugin);
            } catch (Throwable e) {
                throw new PluginException("Cannot create service"
                        + ", interface=" + descriptor.getInterfaceClassName()
                        + ", implementation=" + descriptor.getImplementationClassName()
                        + ", plugin=" + plugin.getId(), e);
            } finally {
                initializing = false;
            }
        }
        return service;
    }
}
