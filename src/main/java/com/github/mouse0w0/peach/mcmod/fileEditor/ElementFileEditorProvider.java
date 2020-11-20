package com.github.mouse0w0.peach.mcmod.fileEditor;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.fileEditor.FileEditorProvider;
import com.github.mouse0w0.peach.fileEditor.WizardFileEditor;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.wizard.Wizard;

import java.nio.file.Path;

public class ElementFileEditorProvider implements FileEditorProvider {
    private final ElementRegistry elementRegistry;

    public ElementFileEditorProvider() {
        this.elementRegistry = Peach.getInstance().getService(ElementRegistry.class);
    }

    @Override
    public boolean accept(Path file) {
        return ElementRegistry.getInstance().getElementType(file) != null;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public FileEditor create(Project project, Path file) {
        ElementManager elementManager = ElementManager.getInstance(project);
        Element element = elementManager.loadElement(file);
        ElementType type = elementRegistry.getElementType(element.getClass());
        Wizard wizard = type.createWizard(project, element);
        FileEditor fileEditor = new WizardFileEditor(file, wizard);
        wizard.addClosedCallback(() -> FileEditorManager.getInstance(project).close(file));
        return fileEditor;
    }
}
