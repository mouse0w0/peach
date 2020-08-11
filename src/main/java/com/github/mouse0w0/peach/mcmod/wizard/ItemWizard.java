package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ItemElement;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemModelStep;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemStep;
import com.github.mouse0w0.peach.ui.wizard.Wizard;

public class ItemWizard extends Wizard {

    private final Element<ItemElement> element;

    public ItemWizard(Element<ItemElement> element) {
        this.element = element;
        element.load();
        addWizardStep(new ItemStep(element));
        addWizardStep(new ItemModelStep(element));
    }

    @Override
    public String getName() {
        String fileName = element.getFile().getFileName().toString();
        return fileName.substring(0, fileName.indexOf('.'));
    }

    @Override
    protected void onFinishWizard() {
        element.save();
    }
}
