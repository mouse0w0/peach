package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.project.Project;
import org.apache.commons.lang3.Validate;

import java.nio.file.Path;

public class ElementType<T extends Element> {
    private final String name;
    private final String translationKey;
    private final Class<T> type;
    private final ElementCreatedHandler<T> createdHandler;
    private final ElementEditorFactory<T> editorFactory;
    private final PreviewGenerator<T> previewGenerator;

    public static <T extends Element> Builder<T> builder(String name, Class<T> type) {
        return new Builder<>(name, type);
    }

    private ElementType(Builder<T> builder) {
        this.name = builder.name;
        this.translationKey = "mod.element." + name;
        this.type = builder.type;
        this.createdHandler = builder.createdHandler;
        this.editorFactory = builder.editorFactory;
        this.previewGenerator = builder.previewGenerator;
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

    public T create(Project project, Path file, String identifier, String name) {
        T instance = newInstance(file);
        if (createdHandler != null) {
            createdHandler.onCreated(project, instance, file, identifier, name);
        }
        return instance;
    }

    public T newInstance(Path file) {
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
        return editorFactory.create(project, element);
    }

    public void generatePreview(Project project, T element, Path outputFile) {
        if (element.getClass() != getType()) {
            throw new IllegalArgumentException("Cannot generate preview for " + element.getClass().getName());
        }
        previewGenerator.generate(project, element, outputFile);
    }

    public static final class Builder<T extends Element> {
        private final String name;
        private final Class<T> type;
        private ElementCreatedHandler<T> createdHandler;
        private ElementEditorFactory<T> editorFactory;
        private PreviewGenerator<T> previewGenerator;

        private Builder(String name, Class<T> type) {
            this.name = Validate.notEmpty(name);
            this.type = Validate.notNull(type);
        }

        public Builder<T> createdHandler(ElementCreatedHandler<T> createdHandler) {
            this.createdHandler = createdHandler;
            return this;
        }

        public Builder<T> editorFactory(ElementEditorFactory<T> editorFactory) {
            this.editorFactory = editorFactory;
            return this;
        }

        public Builder<T> previewGenerator(PreviewGenerator<T> previewGenerator) {
            this.previewGenerator = previewGenerator;
            return this;
        }

        public ElementType<T> build() {
            return new ElementType<>(this);
        }
    }
}
