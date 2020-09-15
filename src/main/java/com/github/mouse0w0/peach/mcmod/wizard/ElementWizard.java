package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.wizard.Wizard;

public abstract class ElementWizard<T> extends Wizard {
    private final Project project;
    private final Element<T> element;

    public ElementWizard(Project project, Element<T> element) {
        this.project = project;
        this.element = element;
    }

    public Project getProject() {
        return project;
    }

    public Element<T> getElement() {
        return element;
    }

    @Override
    public String getName() {
        return element.getName();
    }

    @Override
    protected void onFinishWizard() {
        ElementManager.getInstance(project).saveElement(element);
    }
}
