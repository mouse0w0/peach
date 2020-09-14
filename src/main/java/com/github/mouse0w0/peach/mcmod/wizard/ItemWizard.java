package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ItemElement;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemModelStep;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemStep;

public class ItemWizard extends ElementWizard<ItemElement> {

    public ItemWizard(Element<ItemElement> element) {
        super(element);
        element.load();
        addWizardStep(new ItemStep(element));
        addWizardStep(new ItemModelStep(element));
    }
}
