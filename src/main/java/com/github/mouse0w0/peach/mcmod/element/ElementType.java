package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public class ElementType<T extends Element> {
    private final String name;
    private final String translationKey;
    private final Class<T> type;
    private final ElementEditorFactory<T> elementEditorFactory;
    private final PreviewGenerator<T> previewGenerator;

    public ElementType(String name, Class<T> type, ElementEditorFactory<T> elementEditorFactory, PreviewGenerator<T> previewGenerator) {
        this.name = name;
        this.translationKey = "mod.element." + name;
        this.type = type;
        this.elementEditorFactory = elementEditorFactory;
        this.previewGenerator = previewGenerator;
    }

    public String getName() {
        return name;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public Class<T> getType() {
        return type;
    }

    public T createElement(Path file) {
        try {
            T element = type.newInstance();
            Element.setFile(element, file);
            return element;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cannot create element instance", e);
        }
    }

    public FileEditor createFileEditor(Project project, T element) {
        if (element.getClass() != getType()) {
            throw new IllegalArgumentException("Cannot create wizard for " + element.getClass().getName());
        }
        return elementEditorFactory.create(project, element);
    }

    public void generatePreview(Project project, T element, Path outputFile) {
        if (element.getClass() != getType()) {
            throw new IllegalArgumentException("Cannot generate preview for " + element.getClass().getName());
        }
        previewGenerator.generate(project, element, outputFile);
    }
}
