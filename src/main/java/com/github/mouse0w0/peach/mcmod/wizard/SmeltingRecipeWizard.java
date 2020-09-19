package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.wizard.step.SmeltingRecipeStep;
import com.github.mouse0w0.peach.project.Project;

public class SmeltingRecipeWizard extends ElementWizard<SmeltingRecipe> {

    public SmeltingRecipeWizard(Project project, Element<SmeltingRecipe> element) {
        super(project, element);
        addWizardStep(new SmeltingRecipeStep(element));
    }
}
