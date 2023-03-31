package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.peach.l10n.AppL10n;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Alert extends Dialog<ButtonType> {
    private final Label label;

    public static boolean confirm(String text) {
        return confirm(null, text);
    }

    public static boolean confirm(String title, String text) {
        Alert alert = new Alert(title != null ? title : AppL10n.localize("dialog.confirm.title"),
                text, ButtonType.OK, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
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

        VBox vBox = new VBox(10);
        vBox.setMinSize(300, Region.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(10));

        label = new Label(text, graphic);

        vBox.getChildren().addAll(label, getButtonBar());

        setScene(new Scene(vBox));
    }

    public Label getLabel() {
        return label;
    }
}
