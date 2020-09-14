package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.wizard.step.SmeltingRecipeStep;

public class SmeltingRecipeWizard extends ElementWizard<SmeltingRecipe> {

    public SmeltingRecipeWizard(Element<SmeltingRecipe> element) {
        super(element);
        element.load();
        addWizardStep(new SmeltingRecipeStep(element));
    }
}
