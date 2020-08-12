package com.github.mouse0w0.peach.ui.util;

import com.github.mouse0w0.i18n.I18n;
import javafx.scene.control.TextInputControl;

import java.util.function.Predicate;

public class FXValidator {

    public static boolean validate(TextInputControl control, String message, Predicate<String> validator) {
        if (validator.test(control.getText())) return true;
        Messages.error(I18n.translate(message));
        control.requestFocus();
        control.selectAll();
        return false;
    }

    public static boolean validateNotEmpty(TextInputControl control, String message) {
        return validate(control, message, text -> !text.isEmpty());
    }
}
