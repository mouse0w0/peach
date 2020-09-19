package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.wizard.Wizard;

import java.nio.file.Path;
import java.util.function.Supplier;

public class ElementType<T> {
    private String name;
    private String translationKey;
    private Class<T> type;
    private Supplier<T> elementFactory;
    private WizardFactory<T> wizardFactory;

    public ElementType(String name, Class<T> type, Supplier<T> elementFactory, WizardFactory<T> wizardFactory) {
        this.name = name;
        this.translationKey = "mod.element." + name;
        this.type = type;
        this.elementFactory = elementFactory;
        this.wizardFactory = wizardFactory;
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

    public Element<T> createElement(Path file) {
        return new Element<>(file, this);
    }

    public Wizard createWizard(Project project, Element<?> file) {
        if (file.getType() != this) {
            throw new IllegalArgumentException("Cannot create wizard");
        }
        return wizardFactory.create(project, (Element<T>) file);
    }

    T createElement() {
        try {
            return elementFactory != null ? elementFactory.get() : type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException("Cannot create element");
        }
    }

    public interface WizardFactory<T> {
        Wizard create(Project project, Element<T> element);
    }
}
