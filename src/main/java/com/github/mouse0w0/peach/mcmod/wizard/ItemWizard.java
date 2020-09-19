package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemModelStep;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemStep;
import com.github.mouse0w0.peach.project.Project;

public class ItemWizard extends ElementWizard<ItemElement> {

    public ItemWizard(Project project, Element<ItemElement> element) {
        super(project, element);
        addWizardStep(new ItemStep(element));
        addWizardStep(new ItemModelStep(element));
    }
}
