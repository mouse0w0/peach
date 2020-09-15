package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.mcmod.wizard.CraftingRecipeWizard;
import com.github.mouse0w0.peach.mcmod.wizard.ItemGroupWizard;
import com.github.mouse0w0.peach.mcmod.wizard.ItemWizard;
import com.github.mouse0w0.peach.mcmod.wizard.SmeltingRecipeWizard;

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
        register(new ElementType<>("item", ItemElement.class, ItemElement::new, ItemWizard::new));
        register(new ElementType<>("item_group", ItemGroup.class, ItemGroup::new, ItemGroupWizard::new));
        register(new ElementType<>("crafting_recipe", CraftingRecipe.class, CraftingRecipe::new, CraftingRecipeWizard::new));
        register(new ElementType<>("smelting_recipe", SmeltingRecipe.class, SmeltingRecipe::new, SmeltingRecipeWizard::new));
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
