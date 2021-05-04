package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.nio.file.Files;
import java.nio.file.Path;

public class RenameDialog extends Dialog<Path> {

    private final Path source;

    private final Label label;
    private final TextField editor;

    public RenameDialog(Path source) {
        this(source, FileUtils.getFileName(source));
    }

    public RenameDialog(Path source, String text) {
        super(ButtonType.OK, ButtonType.CANCEL);
        this.source = source;

        setTitle(I18n.translate("dialog.rename.title"));

        boolean isFile = Files.isRegularFile(source);

        VBox vBox = new VBox(10);
        vBox.setMinSize(300, Region.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(10));

        label = new Label(I18n.format(
                isFile ? "dialog.rename.message.file" : "dialog.rename.message.folder", FileUtils.getFileName(source)));
        editor = new TextField(text);

        vBox.getChildren().addAll(label, editor, getButtonBar());

        setScene(new Scene(vBox));
        setResultConverter(buttonType -> {
            ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? getSource().getParent().resolve(editor.getText()) : null;
        });

        Platform.runLater(() -> {
            editor.requestFocus();
            editor.selectRange(0, isFile ? editor.getText().lastIndexOf('.') : editor.getLength());
        });
    }

    public Path getSource() {
        return source;
    }

    public Label getLabel() {
        return label;
    }

    public TextField getEditor() {
        return editor;
    }
}
