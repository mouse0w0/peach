package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.mcmod.element.provider.*;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ElementRegistry {

    private final Map<String, ElementProvider<?>> nameToProviderMap = new LinkedHashMap<>();
    private final Map<Class<?>, ElementProvider<?>> classToProviderMap = new HashMap<>();

    public static ElementRegistry getInstance() {
        return Peach.getInstance().getService(ElementRegistry.class);
    }

    public ElementRegistry() {
        register(new BlockProvider());
        register(new ItemProvider());
        register(new ItemGroupProvider());
        register(new CraftingRecipeProvider());
        register(new SmeltingRecipeProvider());
    }

    public <T extends Element> void register(ElementProvider<T> provider) {
        if (nameToProviderMap.containsKey(provider.getName())) {
            throw new IllegalArgumentException("Element has been registered.");
        }
        nameToProviderMap.put(provider.getName(), provider);
        classToProviderMap.put(provider.getType(), provider);
    }

    public ElementProvider<?> getElementProvider(String name) {
        return nameToProviderMap.get(name);
    }

    public <T extends Element> ElementProvider<T> getElementProvider(Class<T> type) {
        return (ElementProvider<T>) classToProviderMap.get(type);
    }

    public ElementProvider<?> getElementProvider(Path file) {
        String fileName = file.getFileName().toString();

        int start = fileName.indexOf('.');
        int end = fileName.lastIndexOf('.');
        if (start == -1 || end == -1 || start == end) return null;
        String name = fileName.substring(start + 1, end);

        return getElementProvider(name);
    }

    public Collection<ElementProvider<?>> getElementProviders() {
        return nameToProviderMap.values();
    }
}
