package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.fileEditor.WizardFileEditor;
import com.github.mouse0w0.peach.mcmod.element.ElementEditorFactory;
import com.github.mouse0w0.peach.mcmod.element.editor.wizard.CraftingRecipeStep;
import com.github.mouse0w0.peach.mcmod.element.editor.wizard.ElementWizard;
import com.github.mouse0w0.peach.mcmod.element.impl.CraftingRecipe;
import com.github.mouse0w0.peach.project.Project;

public final class CraftingRecipeEditorFactory implements ElementEditorFactory<CraftingRecipe> {
    @Override
    public FileEditor create(Project project, CraftingRecipe element) {
        ElementWizard<CraftingRecipe> wizard = new ElementWizard<>(project, element);
        wizard.addWizardStep(new CraftingRecipeStep(element));
        return new WizardFileEditor(project, element.getFile(), wizard);
    }
}
