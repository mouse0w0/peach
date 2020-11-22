package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.ui.icon.Icons;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RenameDialog {

    private final String defaultValue;

    private final Label label;
    private final TextField editor;

    private final Stage stage = new Stage();

    public RenameDialog() {
        this(null);
    }

    public RenameDialog(String message) {
        this(message, "");
    }

    public RenameDialog(String message, String defaultValue) {
        this.defaultValue = defaultValue;
        stage.setTitle(I18n.translate("dialog.rename.title"));
        stage.getIcons().add(Icons.Peach_16x);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        VBox vBox = new VBox(10);
        vBox.setMinSize(300, Region.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(10));

        label = new Label(message);

        editor = new TextField(defaultValue);

        HBox buttonBar = new HBox(6);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        Button ok = new Button(I18n.translate("common.ok"));
        ok.setDefaultButton(true);
        ok.setOnAction(event -> stage.hide());

        Button cancel = new Button(I18n.translate("common.cancel"));
        cancel.setOnAction(event -> {
            editor.setText(defaultValue);
            stage.hide();
        });
        buttonBar.getChildren().addAll(ok, cancel);

        vBox.getChildren().addAll(label, editor, buttonBar);

        stage.setScene(new Scene(vBox));
        stage.sizeToScene();
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public Label getLabel() {
        return label;
    }

    public TextField getEditor() {
        return editor;
    }

    public void show() {
        stage.show();
    }

    public String showAndWait() {
        stage.showAndWait();
        return editor.getText();
    }
}
