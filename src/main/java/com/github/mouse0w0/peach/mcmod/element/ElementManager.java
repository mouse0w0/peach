package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.mcmod.service.McModService;
import com.github.mouse0w0.peach.mcmod.wizard.CraftingRecipeWizard;
import com.github.mouse0w0.peach.mcmod.wizard.SmeltingRecipeWizard;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ElementManager {

    private final Map<String, ElementDefinition<?>> elementMap = new LinkedHashMap<>();

    public static ElementManager getInstance() {
        return McModService.getInstance().getElementManager();
    }

    public ElementManager() {
        register(new ElementDefinition<>("crafting_recipe", CraftingRecipe.class, CraftingRecipe::new, CraftingRecipeWizard::new));
        register(new ElementDefinition<>("smelting_recipe", SmeltingRecipe.class, SmeltingRecipe::new, SmeltingRecipeWizard::new));
    }

    private <T> void register(ElementDefinition<T> elementDefinition) {
        if (elementMap.containsKey(elementDefinition.getId())) {
            throw new IllegalArgumentException("Element has been registered.");
        }
        elementMap.put(elementDefinition.getId(), elementDefinition);
    }

    public ElementDefinition<?> getElement(String id) {
        return elementMap.get(id);
    }

    public ElementDefinition<?> getElement(Path file) {
        String fileName = file.getFileName().toString();
        String elementId = fileName.substring(fileName.indexOf('.') + 1, fileName.lastIndexOf('.'));
        return elementMap.get(elementId);
    }

    public Collection<ElementDefinition<?>> getElements() {
        return elementMap.values();
    }
}
