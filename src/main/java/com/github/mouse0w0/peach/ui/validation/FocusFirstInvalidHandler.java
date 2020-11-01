package com.github.mouse0w0.peach.ui.validation;

import javafx.scene.Node;
import javafx.scene.control.TextInputControl;

public class FocusFirstInvalidHandler implements ValidationHandler {

    public static final FocusFirstInvalidHandler INSTANCE = new FocusFirstInvalidHandler();

    private FocusFirstInvalidHandler() {
    }

    @Override
    public void onInvalid(Validator validator) {
        Node firstInvalid = validator.getInvalidItems().get(0).getNode();
        firstInvalid.requestFocus();
        if (firstInvalid instanceof TextInputControl) {
            ((TextInputControl) firstInvalid).selectAll();
        }
    }
}
