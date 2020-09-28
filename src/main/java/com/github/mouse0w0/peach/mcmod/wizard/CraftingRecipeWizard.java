package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.impl.CraftingRecipe;
import com.github.mouse0w0.peach.mcmod.wizard.step.CraftingRecipeStep;
import com.github.mouse0w0.peach.project.Project;

public class CraftingRecipeWizard extends ElementWizard<CraftingRecipe> {

    public CraftingRecipeWizard(Project project, CraftingRecipe element) {
        super(project, element);
        addWizardStep(new CraftingRecipeStep(element));
    }
}
