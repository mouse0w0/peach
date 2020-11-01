package com.github.mouse0w0.peach.ui.validation;

public interface ValidationHandler {
    ValidationHandler[] EMPTY = new ValidationHandler[0];

    default void onRegister(Validator.Item item) {
    }

    default void onUnregister(Validator.Item item) {
    }

    default void onReset(Validator validator) {
    }

    default void onValid(Validator validator) {
    }

    default void onInvalid(Validator validator) {
    }
}
