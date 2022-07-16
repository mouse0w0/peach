package com.github.mouse0w0.peach.mcmod.fileEditor;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.fileEditor.FileEditorProvider;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.mcmod.element.provider.ElementProvider;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ElementFileEditorProvider implements FileEditorProvider {
    private final ElementRegistry elementRegistry;

    public ElementFileEditorProvider() {
        this.elementRegistry = Peach.getInstance().getService(ElementRegistry.class);
    }

    @Override
    public boolean accept(Path file) {
        return ElementRegistry.getInstance().getElementProvider(file) != null;
    }

    @Override
    public FileEditor create(Project project, Path file) {
        ElementManager elementManager = ElementManager.getInstance(project);
        Element element = elementManager.loadElement(file);
        ElementProvider provider = elementRegistry.getElementProvider(element.getClass());
        return provider.newEditor(project, element);
    }
}
