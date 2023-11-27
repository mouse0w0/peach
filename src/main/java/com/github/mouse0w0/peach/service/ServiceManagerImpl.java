package com.github.mouse0w0.peach.service;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.message.MessageBus;
import com.github.mouse0w0.peach.message.impl.MessageBusImpl;
import com.github.mouse0w0.peach.plugin.ListenerDescriptor;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginException;
import com.github.mouse0w0.peach.plugin.PluginManagerCore;
import com.github.mouse0w0.peach.service.store.ServiceStore;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ServiceManagerImpl implements ServiceManager {
    protected final ServiceManagerImpl parent;

    private final Map<String, Object> services = new ConcurrentHashMap<>();
    private final Map<String, ServiceWrapper<?>> serviceWrappers = new ConcurrentHashMap<>();
    private final Disposable serviceParentDisposable = Disposer.newDisposable();

    private MessageBusImpl messageBus;

    public ServiceManagerImpl(ServiceManagerImpl parent) {
        this.parent = parent;
        this.messageBus = createMessageBus();
    }

    protected ServiceStore getServiceStore() {
        return getService(ServiceStore.class);
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return getService(serviceClass.getName(), true);
    }

    @Override
    public <T> T getServiceIfCreated(Class<T> serviceClass) {
        return getService(serviceClass.getName(), false);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getService(String serviceClassName, boolean createIfNeeded) {
        T service = (T) services.get(serviceClassName);
        if (service != null) return service;
        ServiceWrapper<T> wrapper = (ServiceWrapper<T>) serviceWrappers.get(serviceClassName);
        if (wrapper != null) {
            service = wrapper.getService(this, createIfNeeded);
            if (service != null) {
                services.putIfAbsent(serviceClassName, service);
            }
            return service;
        }
        if (parent != null) {
            return parent.getService(serviceClassName, createIfNeeded);
        }
        throw new PluginException("Not found service " + serviceClassName);
    }

    @Override
    public MessageBus getMessageBus() {
        return messageBus;
    }

    protected void initialize() {
        Map<String, List<ListenerDescriptor>> lazyListenersMap = new ConcurrentHashMap<>();
        for (Plugin plugin : PluginManagerCore.getEnabledPlugins()) {
            for (ServiceDescriptor service : getPluginServices(plugin)) {
                if (service.isOverride()) {
                    serviceWrappers.put(service.getInterfaceClassName(), new ServiceWrapper<>(plugin, service));
                } else {
                    serviceWrappers.putIfAbsent(service.getInterfaceClassName(), new ServiceWrapper<>(plugin, service));
                }
            }

            for (ListenerDescriptor listener : getPluginListeners(plugin)) {
                listener.setPlugin(plugin);
                lazyListenersMap.computeIfAbsent(listener.getTopic(), k -> new ArrayList<>()).add(listener);
            }
        }
        messageBus.addLazyListeners(lazyListenersMap);
    }

    protected void preloadServices() {
        List<CompletableFuture<Void>> syncFutures = new ArrayList<>();
        for (Plugin plugin : PluginManagerCore.getEnabledPlugins()) {
            for (ServiceDescriptor service : getPluginServices(plugin)) {
                switch (service.getPreload()) {
                    case TRUE -> preloadService(service.getInterfaceClassName());
                    case AWAIT -> syncFutures.add(preloadService(service.getInterfaceClassName()));
                }
            }
        }
        for (CompletableFuture<Void> syncFuture : syncFutures) {
            syncFuture.join();
        }
    }

    private CompletableFuture<Void> preloadService(String serviceClassName) {
        return CompletableFuture.runAsync(() -> {
            ServiceWrapper<?> wrapper = serviceWrappers.get(serviceClassName);
            if (wrapper != null) {
                services.putIfAbsent(serviceClassName, wrapper.getService(this, true));
            }
        });
    }

    protected abstract MessageBusImpl createMessageBus();

    protected abstract List<ServiceDescriptor> getPluginServices(Plugin plugin);

    protected abstract List<ListenerDescriptor> getPluginListeners(Plugin plugin);

    @SuppressWarnings("unchecked")
    <T> T newServiceInstance(Class<?> implementationClass, Plugin plugin) throws Throwable {
        Constructor<?>[] constructors = implementationClass.getConstructors();
        if (constructors.length != 1) {
            throw new PluginException("Too many constructors in service"
                    + ", implementation=" + implementationClass.getName()
                    + ", plugin=" + plugin.getId());
        }
        Constructor<?> constructor = constructors[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        int parameterCount = constructor.getParameterCount();
        Object[] parameters = new Object[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            parameters[i] = resolveParameter(parameterTypes[i]);
        }
        T instance = (T) constructor.newInstance(parameters);
        if (instance instanceof Disposable) {
            Disposer.register(this, (Disposable) instance);
        }
        if (instance instanceof PersistentService) {
            getServiceStore().loadService((PersistentService) instance);
        }
        return instance;
    }

    Object resolveParameter(Class<?> parameterType) {
        if (parameterType.isAssignableFrom(getClass())) {
            return this;
        }

        return getService(parameterType);
    }

    protected void saveServices() {
        ServiceStore serviceStore = getServiceStore();
        for (Object service : services.values()) {
            if (service instanceof PersistentService) {
                serviceStore.saveService((PersistentService) service);
            }
        }
    }

    @Override
    public void dispose() {
        Disposer.dispose(serviceParentDisposable);
        services.clear();

        if (messageBus != null) {
            Disposer.dispose(messageBus);
            messageBus = null;
        }
    }
}
