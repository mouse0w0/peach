package com.github.mouse0w0.peach.forge.element;

import com.github.mouse0w0.peach.ui.wizard.Wizard;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

public class ElementDefinition<T> {
    private String id;
    private String translationKey;
    private Class<T> type;
    private Supplier<T> elementFactory;
    private Function<ElementFile<T>, Wizard> wizardFactory;

    public ElementDefinition(String id, Class<T> type, Supplier<T> elementFactory, Function<ElementFile<T>, Wizard> wizardFactory) {
        this.id = id;
        this.translationKey = "mod.element." + id;
        this.type = type;
        this.elementFactory = elementFactory;
        this.wizardFactory = wizardFactory;
    }

    public String getId() {
        return id;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public Class<T> getType() {
        return type;
    }

    public ElementFile<T> load(Path file) {
        return new ElementFile<>(file, this);
    }

    public Wizard createWizard(ElementFile<?> file) {
        if (file.getDefinition() != this) {
            throw new IllegalArgumentException("Cannot create wizard");
        }
        return wizardFactory.apply((ElementFile<T>) file);
    }

    T createElement() {
        try {
            return elementFactory != null ? elementFactory.get() : type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException("Cannot create element");
        }
    }
}
