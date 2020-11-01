package com.github.mouse0w0.peach.ui.validation;

import com.github.mouse0w0.peach.ui.util.Alerts;

public class ShowInvalidDialogHandler implements ValidationHandler {

    public static final ShowInvalidDialogHandler INSTANCE = new ShowInvalidDialogHandler();

    private ShowInvalidDialogHandler() {
    }

    @Override
    public void onInvalid(Validator validator) {
        StringBuilder message = new StringBuilder();
        for (Validator.Item item : validator.getInvalidItems()) {
            message.append(String.format(item.getMessage(), item.getValue())).append("\n");
        }
        Alerts.error(message.toString());
    }
}
