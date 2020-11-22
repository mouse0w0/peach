package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.i18n.I18n;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RenameDialog extends MyDialog<String> {

    private final Label label;
    private final TextField editor;

    public RenameDialog() {
        this(null);
    }

    public RenameDialog(String message) {
        this(message, "");
    }

    public RenameDialog(String message, String text) {
        super(ButtonType.OK, ButtonType.CANCEL);
        setTitle(I18n.translate("dialog.rename.title"));

        VBox vBox = new VBox(10);
        vBox.setMinSize(300, Region.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(10));

        label = new Label(message);
        editor = new TextField(text);

        vBox.getChildren().addAll(label, editor, getButtonBar());

        setScene(new Scene(vBox));
        setResultConverter(buttonType -> {
            ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? editor.getText() : null;
        });
    }

    public Label getLabel() {
        return label;
    }

    public TextField getEditor() {
        return editor;
    }
}
