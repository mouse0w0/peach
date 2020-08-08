package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.ElementFile;
import com.github.mouse0w0.peach.mcmod.element.ItemElement;
import com.github.mouse0w0.peach.mcmod.wizard.step.ItemStep;
import com.github.mouse0w0.peach.ui.wizard.Wizard;

public class ItemWizard extends Wizard {

    private final ElementFile<ItemElement> file;

    public ItemWizard(ElementFile<ItemElement> file) {
        this.file = file;
        file.load();
        addWizardStep(new ItemStep(file));
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
