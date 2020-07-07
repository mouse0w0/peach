package com.github.mouse0w0.peach.forge.wizard;

import com.github.mouse0w0.peach.forge.element.CraftingRecipe;
import com.github.mouse0w0.peach.forge.element.ElementFile;
import com.github.mouse0w0.peach.ui.forge.CraftingRecipeUI;
import com.github.mouse0w0.peach.ui.wizard.Wizard;

public class CraftingRecipeWizard extends Wizard {

    private final ElementFile<CraftingRecipe> file;

    public CraftingRecipeWizard(ElementFile<CraftingRecipe> file) {
        this.file = file;
        addWizardStep(new CraftingRecipeUI());
    }

    @Override
    public String getName() {
        String fileName = file.getFile().getFileName().toString();
        return fileName.substring(0, fileName.indexOf('.'));
    }

    public ElementFile<CraftingRecipe> getFile() {
        return file;
    }
}
