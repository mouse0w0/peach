package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.ui.control.ButtonType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Alert extends Dialog<ButtonType> {
    private final Label label;

    public static boolean confirm(String text) {
        return confirm(null, text);
    }

    public static boolean confirm(String title, String text) {
        return new Alert(title != null ? title : AppL10n.localize("dialog.confirm.title"), text, ButtonType.OK, ButtonType.CANCEL)
                .showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public static void warning(String text) {
        warning(null, text);
    }

    public static void warning(String title, String text) {
        new Alert(title != null ? title : AppL10n.localize("dialog.warning.title"), text, ButtonType.OK).show();
    }

    public static void error(String text) {
        error(null, text);
    }

    public static void error(String title, String text) {
        new Alert(title != null ? title : AppL10n.localize("dialog.error.title"), text, ButtonType.OK).show();
    }

    public Alert(String title, String text, ButtonType... buttonTypes) {
        this(title, text, null, buttonTypes);
    }

    public Alert(String title, String text, Node graphic, ButtonType... buttonTypes) {
        super(buttonTypes);
        setTitle(title);

        label = new Label(text, graphic);

        VBox vBox = new VBox(label, getButtonBar());
        vBox.setMinWidth(300);
        vBox.setSpacing(8);
        vBox.setPadding(new Insets(8));

        setScene(new Scene(vBox));
    }

    public Label getLabel() {
        return label;
    }
}
