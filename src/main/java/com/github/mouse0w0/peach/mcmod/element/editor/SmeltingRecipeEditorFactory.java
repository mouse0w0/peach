package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.fileEditor.WizardFileEditor;
import com.github.mouse0w0.peach.mcmod.element.ElementEditorFactory;
import com.github.mouse0w0.peach.mcmod.element.editor.wizard.ElementWizard;
import com.github.mouse0w0.peach.mcmod.element.editor.wizard.SmeltingRecipeStep;
import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingRecipe;
import com.github.mouse0w0.peach.project.Project;

public final class SmeltingRecipeEditorFactory implements ElementEditorFactory<SmeltingRecipe> {
    @Override
    public FileEditor create(Project project, SmeltingRecipe element) {
        ElementWizard<SmeltingRecipe> wizard = new ElementWizard<>(project, element);
        wizard.addWizardStep(new SmeltingRecipeStep(element));
        return new WizardFileEditor(project, element.getFile(), wizard);
    }
}
