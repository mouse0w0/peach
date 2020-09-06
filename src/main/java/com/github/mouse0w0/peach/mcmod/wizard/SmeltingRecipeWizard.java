package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.wizard.step.SmeltingRecipeStep;
import com.github.mouse0w0.peach.wizard.Wizard;

public class SmeltingRecipeWizard extends Wizard {
    private final Element<SmeltingRecipe> file;

    public SmeltingRecipeWizard(Element<SmeltingRecipe> file) {
        this.file = file;
        file.load();
        addWizardStep(new SmeltingRecipeStep(file));
    }

    @Override
    public String getName() {
        String fileName = file.getFile().getFileName().toString();
        return fileName.substring(0, fileName.indexOf('.'));
    }

    @Override
    protected void onFinishWizard() {
        file.save();
    }
}
