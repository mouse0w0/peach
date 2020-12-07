package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.fileEditor.WizardFileEditor;
import com.github.mouse0w0.peach.mcmod.element.ElementEditorFactory;
import com.github.mouse0w0.peach.mcmod.element.editor.wizard.ElementWizard;
import com.github.mouse0w0.peach.mcmod.element.editor.wizard.ItemGroupStep;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroup;
import com.github.mouse0w0.peach.project.Project;

public final class ItemGroupEditorFactory implements ElementEditorFactory<ItemGroup> {
    @Override
    public FileEditor create(Project project, ItemGroup element) {
        ElementWizard<ItemGroup> wizard = new ElementWizard<>(project, element);
        wizard.addWizardStep(new ItemGroupStep(project, element));
        return new WizardFileEditor(project, element.getFile(), wizard);
    }
}
