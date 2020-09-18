package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.Peach;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ElementRegistry {

    private final Map<String, ElementType<?>> elementMap = new LinkedHashMap<>();

    public static ElementRegistry getInstance() {
        return Peach.getInstance().getService(ElementRegistry.class);
    }

    public ElementRegistry() {
        register(ElementTypes.ITEM);
        register(ElementTypes.ITEM_GROUP);
        register(ElementTypes.CRAFTING_RECIPE);
        register(ElementTypes.SMELTING_RECIPE);
    }

    public <T> void register(ElementType<T> elementType) {
        if (elementMap.containsKey(elementType.getId())) {
            throw new IllegalArgumentException("Element has been registered.");
        }
        elementMap.put(elementType.getId(), elementType);
    }

    public ElementType<?> getElementType(String id) {
        return elementMap.get(id);
    }

    public ElementType<?> getElementType(Path file) {
        String fileName = file.getFileName().toString();

        int start = fileName.indexOf('.');
        int end = fileName.lastIndexOf('.');
        if (start == -1 || end == -1 || start == end) return null;
        String elementId = fileName.substring(start + 1, end);

        return elementMap.get(elementId);
    }

    public Collection<ElementType<?>> getElementTypes() {
        return elementMap.values();
    }
}
