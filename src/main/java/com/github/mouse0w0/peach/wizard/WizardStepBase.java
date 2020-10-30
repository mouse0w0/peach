package com.github.mouse0w0.peach.wizard;

import com.github.mouse0w0.peach.ui.validation.Validator;
import javafx.scene.Node;

import java.util.function.Predicate;

public abstract class WizardStepBase implements WizardStep {

    private final Validator validator = new Validator();

    private Node content;

    protected <T> void register(Node node, Predicate<T> predicate, String message) {
        validator.register(node, predicate, message);
    }

    protected void unregister(Node node) {
        validator.unregister(node);
    }

    @Override
    public Node getContent() {
        return content;
    }

    protected void setContent(Node content) {
        this.content = content;
    }

    @Override
    public boolean validate() {
        if (!validator.validate()) {
            validator.showInvalidDialog();
            validator.focusFirstInvalid();
            return false;
        }
        return true;
    }
}
