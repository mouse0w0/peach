package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.peach.javafx.control.ButtonType;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TextInputDialog extends Dialog<String> {

    private final Label label;
    private final TextField editor;

    public TextInputDialog(String title, String message) {
        this(title, message, "");
    }

    public TextInputDialog(String title, String message, String text) {
        super(ButtonType.OK, ButtonType.CANCEL);
        setTitle(title);

        VBox vBox = new VBox(10);
        vBox.setMinSize(300, Region.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(10));

        label = new Label(message);
        label.managedProperty().bind(Bindings.createBooleanBinding(
                () -> label.getText() != null || label.getGraphic() != null,
                label.textProperty(), label.graphicProperty()));
        editor = new TextField(text);

        vBox.getChildren().addAll(label, editor, getButtonBar());

        setScene(new Scene(vBox));
        setResultConverter(buttonType -> {
            ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? editor.getText() : null;
        });

        Platform.runLater(() -> {
            editor.requestFocus();
            editor.selectAll();
        });
    }

    public Label getLabel() {
        return label;
    }

    public TextField getEditor() {
        return editor;
    }
}
