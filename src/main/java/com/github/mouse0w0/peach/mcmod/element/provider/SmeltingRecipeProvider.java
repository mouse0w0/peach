package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.element.editor.SmeltingRecipeEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingElement;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public class SmeltingRecipeProvider extends ElementProvider<SmeltingElement> {
    public SmeltingRecipeProvider() {
        super(SmeltingElement.class, "smelting");
    }

    @Override
    public SmeltingElement newElement(Project project, Path file, String identifier, String name) {
        final SmeltingElement smelting = new SmeltingElement();
        smelting.setFile(file);
        return smelting;
    }

    @Override
    public FileEditor newEditor(Project project, SmeltingElement element) {
        return new SmeltingRecipeEditor(project, element);
    }
}
