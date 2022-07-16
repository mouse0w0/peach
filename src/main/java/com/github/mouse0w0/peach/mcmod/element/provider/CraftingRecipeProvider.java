package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.element.editor.CraftingRecipeEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.MECraftingRecipe;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public class CraftingRecipeProvider extends ElementProvider<MECraftingRecipe> {
    public CraftingRecipeProvider() {
        super(MECraftingRecipe.class, "crafting");
    }

    @Override
    public MECraftingRecipe newElement(Project project, Path file, String identifier, String name) {
        final MECraftingRecipe crafting = new MECraftingRecipe();
        crafting.setFile(file);
        crafting.setIdentifier(identifier);
        return crafting;
    }

    @Override
    public FileEditor newEditor(Project project, MECraftingRecipe element) {
        return new CraftingRecipeEditor(project, element);
    }
}
