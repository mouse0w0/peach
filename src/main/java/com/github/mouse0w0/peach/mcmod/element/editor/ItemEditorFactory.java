package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.fileEditor.WizardFileEditor;
import com.github.mouse0w0.peach.mcmod.element.ElementEditorFactory;
import com.github.mouse0w0.peach.mcmod.element.editor.wizard.ElementWizard;
import com.github.mouse0w0.peach.mcmod.element.editor.wizard.ItemModelStep;
import com.github.mouse0w0.peach.mcmod.element.editor.wizard.ItemPropertiesStep;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.project.Project;

public final class ItemEditorFactory implements ElementEditorFactory<ItemElement> {
    @Override
    public FileEditor create(Project project, ItemElement element) {
        ElementWizard<ItemElement> wizard = new ElementWizard<>(project, element);
        wizard.addWizardStep(new ItemPropertiesStep(element));
        wizard.addWizardStep(new ItemModelStep(element));
        return new WizardFileEditor(project, element.getFile(), wizard);
    }
}
