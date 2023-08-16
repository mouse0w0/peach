package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.element.editor.CraftingRecipeEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.CraftingElement;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public class CraftingRecipeProvider extends ElementProvider<CraftingElement> {
    public CraftingRecipeProvider() {
        super(CraftingElement.class, "crafting");
    }

    @Override
    public CraftingElement newElement(Project project, Path file, String identifier, String name) {
        final CraftingElement crafting = new CraftingElement();
        crafting.setFile(file);
        crafting.setIdentifier(identifier);
        return crafting;
    }

    @Override
    public FileEditor newEditor(Project project, CraftingElement element) {
        return new CraftingRecipeEditor(project, element);
    }
}
