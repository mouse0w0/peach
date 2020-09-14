package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ItemGroup;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemGroupStep;

public class ItemGroupWizard extends ElementWizard<ItemGroup> {

    public ItemGroupWizard(Element<ItemGroup> element) {
        super(element);
        element.load();
        addWizardStep(new ItemGroupStep(element));
    }
}
