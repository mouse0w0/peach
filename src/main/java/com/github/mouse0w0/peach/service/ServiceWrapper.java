package com.github.mouse0w0.peach.service;

import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginException;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

final class ServiceWrapper<T> {
    private static final VarHandle SERVICE;

    static {
        try {
            SERVICE = MethodHandles.lookup().findVarHandle(ServiceWrapper.class, "service", Object.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Plugin plugin;
    private final ServiceDescriptor descriptor;

    private volatile T service;
    private boolean initializing;

    public ServiceWrapper(Plugin plugin, ServiceDescriptor descriptor) {
        this.plugin = plugin;
        this.descriptor = descriptor;
    }

    @SuppressWarnings("unchecked")
    public T getService(ServiceManagerImpl serviceManager, boolean createIfNeeded) {
        T service = (T) SERVICE.getAcquire(this);
        if (service != null) return service;

        synchronized (this) {
            service = (T) SERVICE.getAcquire(this);
            if (service != null) return service;

            if (!createIfNeeded) return null;

            if (initializing)
                throw new PluginException("Cyclic service initialization"
                        + ", interface=" + descriptor.getInterfaceClassName()
                        + ", implementation=" + descriptor.getImplementationClassName()
                        + ", plugin=" + plugin.getId());

            try {
                initializing = true;
                Class<?> serviceClass = plugin.getClassLoader().loadClass(descriptor.getImplementationClassName());
                service = serviceManager.newServiceInstance(serviceClass, plugin);
                SERVICE.setRelease(this, service);
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
