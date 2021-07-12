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
import javafx.stage.WindowEvent;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class RenameDialog<T> extends Dialog<T> {

    private final String rawName;
    private final boolean isDirectory;

    private final Label label;
    private final TextField editor;

    public static RenameDialog<File> create(File file) {
        RenameDialog<File> dialog = new RenameDialog<>(file.getName(), file.getName(), file.isDirectory());
        dialog.setResultConverter(buttonType -> {
            ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? new File(file.getParent(), dialog.getNewName()) : null;
        });
        return dialog;
    }

    public static RenameDialog<File> create(File file, String newName) {
        RenameDialog<File> dialog = new RenameDialog<>(file.getName(), newName, file.isDirectory());
        dialog.setResultConverter(buttonType -> {
            ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? new File(file.getParent(), dialog.getNewName()) : null;
        });
        return dialog;
    }

    public static RenameDialog<Path> create(Path path) {
        RenameDialog<Path> dialog = new RenameDialog<>(FileUtils.getFileName(path), FileUtils.getFileName(path), Files.isDirectory(path));
        dialog.setResultConverter(buttonType -> {
            ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? path.getParent().resolve(dialog.getNewName()) : null;
        });
        return dialog;
    }

    public static RenameDialog<Path> create(Path path, String newName) {
        RenameDialog<Path> dialog = new RenameDialog<>(FileUtils.getFileName(path), newName, Files.isDirectory(path));
        dialog.setResultConverter(buttonType -> {
            ButtonBar.ButtonData data = buttonType == null ? null : buttonType.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? path.getParent().resolve(dialog.getNewName()) : null;
        });
        return dialog;
    }

    public RenameDialog(String rawName, String newName, boolean isDirectory) {
        super(ButtonType.OK, ButtonType.CANCEL);
        this.rawName = rawName;
        this.isDirectory = isDirectory;

        setTitle(I18n.translate("dialog.rename.title"));

        VBox vBox = new VBox(10);
        vBox.setMinSize(300, Region.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(10));

        label = new Label(I18n.format(
                isDirectory ? "dialog.rename.message.folder" : "dialog.rename.message.file", rawName));
        editor = new TextField(newName);

        vBox.getChildren().addAll(label, editor, getButtonBar());

        setScene(new Scene(vBox));

        addEventFilter(WindowEvent.WINDOW_SHOWN, event -> Platform.runLater(() -> {
            editor.requestFocus();
            editor.selectRange(0, isDirectory ? editor.getLength() : editor.getText().indexOf('.'));
        }));
    }

    public String getRawName() {
        return rawName;
    }

    public String getNewName() {
        return editor.getText();
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public Label getLabel() {
        return label;
    }

    public TextField getEditor() {
        return editor;
    }
}
