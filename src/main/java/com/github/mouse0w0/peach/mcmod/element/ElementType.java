package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.wizard.Wizard;

import java.nio.file.Path;
import java.util.function.Supplier;

public class ElementType<T extends Element> {
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

    public T createElement(Path file) {
        T element = elementFactory.get();
        Element.setFile(element, file);
        return element;
    }

    public Wizard createWizard(Project project, T element) {
        if (element.getClass() != getType()) {
            throw new IllegalArgumentException("Cannot create wizard for " + element.getClass().getName());
        }
        return wizardFactory.create(project, element);
    }

    public interface WizardFactory<T extends Element> {
        Wizard create(Project project, T element);
    }
}
