package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.CraftingRecipe;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.wizard.step.CraftingRecipeStep;

public class CraftingRecipeWizard extends ElementWizard<CraftingRecipe> {

    public CraftingRecipeWizard(Element<CraftingRecipe> element) {
        super(element);
        element.load();
        addWizardStep(new CraftingRecipeStep(element));
    }
}
