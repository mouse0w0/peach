package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ItemGroup;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemGroupStep;
import com.github.mouse0w0.peach.wizard.Wizard;

public class ItemGroupWizard extends Wizard {

    private final Element<ItemGroup> element;

    public ItemGroupWizard(Element<ItemGroup> element) {
        this.element = element;
        element.load();
        addWizardStep(new ItemGroupStep(element));
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
