package com.github.mouse0w0.peach.wizard;

import com.github.mouse0w0.peach.ui.validation.FocusFirstInvalidHandler;
import com.github.mouse0w0.peach.ui.validation.ShowInvalidDialogHandler;
import com.github.mouse0w0.peach.ui.validation.Validator;
import javafx.scene.Node;

import java.util.function.Predicate;

public abstract class WizardStepBase implements WizardStep {

    private final Validator validator = createDefaultValidator();

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

    protected Validator createDefaultValidator() {
        return new Validator(ShowInvalidDialogHandler.INSTANCE, FocusFirstInvalidHandler.INSTANCE);
    }

    @Override
    public boolean validate() {
        return validator.validate();
    }
}
