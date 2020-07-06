package com.github.mouse0w0.peach.forge.element;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ElementManager {

    private final Map<String, Element> elementMap = new LinkedHashMap<>();

    public ElementManager() {
        register(Element.builder()
                .id("crafting_recipe")
                .translationKey("mod.element.crafting_recipe")
                .type(CraftingRecipe.class)
                .build());
    }

    private void register(Element element) {
        if (elementMap.containsKey(element.getId())) {
            throw new IllegalArgumentException("Element has been registered.");
        }
        elementMap.put(element.getId(), element);
    }

    public Collection<Element> getElements() {
        return elementMap.values();
    }
}
