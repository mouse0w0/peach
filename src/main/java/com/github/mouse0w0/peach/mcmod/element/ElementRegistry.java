package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.Peach;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ElementRegistry {

    private final Map<String, ElementType<?>> elementMap = new LinkedHashMap<>();
    private final Map<Class<?>, ElementType<?>> classToElementMap = new HashMap<>();

    public static ElementRegistry getInstance() {
        return Peach.getInstance().getService(ElementRegistry.class);
    }

    public ElementRegistry() {
        register(ElementTypes.BLOCK);
        register(ElementTypes.ITEM);
        register(ElementTypes.ITEM_GROUP);
        register(ElementTypes.CRAFTING_RECIPE);
        register(ElementTypes.SMELTING_RECIPE);
    }

    public <T extends Element> void register(ElementType<T> elementType) {
        if (elementMap.containsKey(elementType.getName())) {
            throw new IllegalArgumentException("Element has been registered.");
        }
        elementMap.put(elementType.getName(), elementType);
        classToElementMap.put(elementType.getType(), elementType);
    }

    public ElementType<?> getElementType(String name) {
        return elementMap.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Element> ElementType<T> getElementType(Class<T> type) {
        return (ElementType<T>) classToElementMap.get(type);
    }

    public ElementType<?> getElementType(Path file) {
        String fileName = file.getFileName().toString();

        int start = fileName.indexOf('.');
        int end = fileName.lastIndexOf('.');
        if (start == -1 || end == -1 || start == end) return null;
        String name = fileName.substring(start + 1, end);

        return getElementType(name);
    }

    public Collection<ElementType<?>> getElementTypes() {
        return elementMap.values();
    }
}
