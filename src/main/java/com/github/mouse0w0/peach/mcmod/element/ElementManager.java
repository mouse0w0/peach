package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.mcmod.service.McModService;
import com.github.mouse0w0.peach.mcmod.wizard.CraftingRecipeWizard;
import com.github.mouse0w0.peach.mcmod.wizard.ItemWizard;
import com.github.mouse0w0.peach.mcmod.wizard.SmeltingRecipeWizard;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ElementManager {

    private final Map<String, ElementType<?>> elementMap = new LinkedHashMap<>();

    public static ElementManager getInstance() {
        return McModService.getInstance().getElementManager();
    }

    public ElementManager() {
        register(new ElementType<>("item", ItemElement.class, ItemElement::new, ItemWizard::new));
        register(new ElementType<>("crafting_recipe", CraftingRecipe.class, CraftingRecipe::new, CraftingRecipeWizard::new));
        register(new ElementType<>("smelting_recipe", SmeltingRecipe.class, SmeltingRecipe::new, SmeltingRecipeWizard::new));
    }

    private <T> void register(ElementType<T> elementType) {
        if (elementMap.containsKey(elementType.getId())) {
            throw new IllegalArgumentException("Element has been registered.");
        }
        elementMap.put(elementType.getId(), elementType);
    }

    public ElementType<?> getElement(String id) {
        return elementMap.get(id);
    }

    public ElementType<?> getElement(Path file) {
        String fileName = file.getFileName().toString();
        String elementId = fileName.substring(fileName.indexOf('.') + 1, fileName.lastIndexOf('.'));
        return elementMap.get(elementId);
    }

    public Collection<ElementType<?>> getElements() {
        return elementMap.values();
    }
}
