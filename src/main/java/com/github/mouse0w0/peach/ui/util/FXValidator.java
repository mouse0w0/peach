package com.github.mouse0w0.peach.ui.util;

import com.github.mouse0w0.i18n.I18n;
import javafx.scene.control.TextInputControl;

import java.util.function.Predicate;

public class FXValidator {

    public static boolean validate(TextInputControl control, String message, Predicate<String> validator) {
        String text = control.getText();
        if (validator.test(text)) return true;
        Messages.error(String.format(I18n.translate(message), text));
        control.requestFocus();
        control.selectAll();
        return false;
    }

    public static boolean validateNotEmpty(TextInputControl control, String message) {
        return validate(control, message, text -> !text.isEmpty());
    }
}
