package com.github.mouse0w0.peach.mcmod.element.editor.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.wizard.Wizard;

public class ElementWizard<T extends Element> extends Wizard {
    private final Project project;
    private final T element;

    public ElementWizard(Project project, T element) {
        this.project = project;
        this.element = element;
    }

    public Project getProject() {
        return project;
    }

    public T getElement() {
        return element;
    }

    @Override
    public String getName() {
        return element.getFileName();
    }

    @Override
    protected void onFinishWizard() {
        ElementManager.getInstance(project).saveElement(element);
    }
}
