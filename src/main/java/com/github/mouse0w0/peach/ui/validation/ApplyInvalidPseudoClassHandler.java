package com.github.mouse0w0.peach.ui.validation;

import javafx.css.PseudoClass;

public class ApplyInvalidPseudoClassHandler implements ValidationHandler {

    public static final ApplyInvalidPseudoClassHandler INSTNACE = new ApplyInvalidPseudoClassHandler();

    public static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

    private ApplyInvalidPseudoClassHandler() {
    }

    @Override
    public void onReset(Validator validator) {
        for (Validator.Item item : validator.getItems()) {
            item.getNode().pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        }
    }

    @Override
    public void onInvalid(Validator validator) {
        for (Validator.Item item : validator.getInvalidItems()) {
            item.getNode().pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }
}
