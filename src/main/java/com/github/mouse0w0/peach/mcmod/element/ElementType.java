package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.wizard.Wizard;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

public class ElementType<T> {
    private String id;
    private String translationKey;
    private Class<T> type;
    private Supplier<T> elementFactory;
    private Function<Element<T>, Wizard> wizardFactory;

    public ElementType(String id, Class<T> type, Supplier<T> elementFactory, Function<Element<T>, Wizard> wizardFactory) {
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

    public Element<T> load(Path file) {
        return new Element<>(file, this);
    }

    public Wizard createWizard(Element<?> file) {
        if (file.getType() != this) {
            throw new IllegalArgumentException("Cannot create wizard");
        }
        return wizardFactory.apply((Element<T>) file);
    }

    T createElement() {
        try {
            return elementFactory != null ? elementFactory.get() : type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException("Cannot create element");
        }
    }
}
