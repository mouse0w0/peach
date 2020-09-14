package com.github.mouse0w0.peach.mcmod.wizard;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.wizard.Wizard;

public abstract class ElementWizard<T> extends Wizard {
    protected final Element<T> element;

    public ElementWizard(Element<T> element) {
        this.element = element;
    }

    @Override
    public String getName() {
        return element.getName();
    }

    @Override
    protected void onFinishWizard() {
        element.save();
    }
}
