package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemModelStep;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemPropertiesStep;
import com.github.mouse0w0.peach.project.Project;

public class ItemWizard extends ElementWizard<ItemElement> {

    public ItemWizard(Project project, ItemElement element) {
        super(project, element);
        addWizardStep(new ItemPropertiesStep(element));
        addWizardStep(new ItemModelStep(element));
    }
}
