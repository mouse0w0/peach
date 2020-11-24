package com.github.mouse0w0.peach.dialog;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Alert extends MyDialog<ButtonType> {
    private final Label label;

    public static boolean confirm(String title, String text) {
        Alert alert = new Alert(title, text, ButtonType.YES, ButtonType.NO);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
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
