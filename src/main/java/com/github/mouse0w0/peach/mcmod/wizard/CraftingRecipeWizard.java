package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.CraftingRecipe;
import com.github.mouse0w0.peach.mcmod.element.ElementFile;
import com.github.mouse0w0.peach.mcmod.wizard.step.CraftingRecipeStep;
import com.github.mouse0w0.peach.ui.wizard.Wizard;

public class CraftingRecipeWizard extends Wizard {

    private final ElementFile<CraftingRecipe> file;

    public CraftingRecipeWizard(ElementFile<CraftingRecipe> file) {
        this.file = file;
        file.load();
        addWizardStep(new CraftingRecipeStep(file));
    }

    @Override
    public String getName() {
        String fileName = file.getFile().getFileName().toString();
        return fileName.substring(0, fileName.indexOf('.'));
    }

    public ElementFile<CraftingRecipe> getFile() {
        return file;
    }

    @Override
    protected void onFinishWizard() {
        getFile().save();
    }
}
