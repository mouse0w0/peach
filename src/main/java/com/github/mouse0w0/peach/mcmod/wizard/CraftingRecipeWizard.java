package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.CraftingRecipe;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.wizard.step.CraftingRecipeStep;
import com.github.mouse0w0.peach.wizard.Wizard;

public class CraftingRecipeWizard extends Wizard {

    private final Element<CraftingRecipe> file;

    public CraftingRecipeWizard(Element<CraftingRecipe> file) {
        this.file = file;
        file.load();
        addWizardStep(new CraftingRecipeStep(file));
    }

    @Override
    public String getName() {
        String fileName = file.getFile().getFileName().toString();
        return fileName.substring(0, fileName.indexOf('.'));
    }

    public Element<CraftingRecipe> getFile() {
        return file;
    }

    @Override
    protected void onFinishWizard() {
        getFile().save();
    }
}
