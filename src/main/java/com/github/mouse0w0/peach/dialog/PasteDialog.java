package com.github.mouse0w0.peach.dialog;

import javafx.scene.control.ButtonBar;

public class PasteDialog extends Alert {

    public static final ButtonType OVERWRITE =
            new ButtonType("dialog.button.overwrite", ButtonBar.ButtonData.OK_DONE);

    public static final ButtonType RENAME =
            new ButtonType("dialog.button.rename", ButtonBar.ButtonData.APPLY);

    public static final ButtonType SKIP =
            new ButtonType("dialog.button.skip", ButtonBar.ButtonData.CANCEL_CLOSE);

    public static final ButtonType OVERWRITE_ALL =
            new ButtonType("dialog.button.overwriteAll", ButtonBar.ButtonData.RIGHT);

    public static final ButtonType SKIP_ALL =
            new ButtonType("dialog.button.skipAll", ButtonBar.ButtonData.RIGHT);

    public PasteDialog(String title, String text, boolean multiple) {
        super(title, text);
        getButtonBar().setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);
        if (multiple) {
            getButtons().setAll(OVERWRITE, RENAME, SKIP, OVERWRITE_ALL, SKIP_ALL);
        } else {
            getButtons().setAll(OVERWRITE, RENAME, SKIP);
        }
    }
}
