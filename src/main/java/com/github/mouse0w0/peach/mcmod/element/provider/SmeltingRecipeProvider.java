package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.element.editor.SmeltingRecipeEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.MESmeltingRecipe;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public class SmeltingRecipeProvider extends ElementProvider<MESmeltingRecipe> {
    public SmeltingRecipeProvider() {
        super(MESmeltingRecipe.class, "smelting");
    }

    @Override
    public MESmeltingRecipe newElement(Project project, Path file, String identifier, String name) {
        final MESmeltingRecipe smelting = new MESmeltingRecipe();
        smelting.setFile(file);
        return smelting;
    }

    @Override
    public FileEditor newEditor(Project project, MESmeltingRecipe element) {
        return new SmeltingRecipeEditor(project, element);
    }
}
