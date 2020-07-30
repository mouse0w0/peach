package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.ElementFile;
import com.github.mouse0w0.peach.mcmod.element.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.wizard.step.SmeltingRecipeStep;
import com.github.mouse0w0.peach.ui.wizard.Wizard;

public class SmeltingRecipeWizard extends Wizard {
    private final ElementFile<SmeltingRecipe> file;

    public SmeltingRecipeWizard(ElementFile<SmeltingRecipe> file) {
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
