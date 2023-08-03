package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.peach.javafx.control.ButtonType;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class RenameDialog<T> extends Dialog<T> {

    public static RenameDialog<Path> create(Path path) {
        return create(path, FileUtils.getFileName(path));
    }

    public static RenameDialog<Path> create(Path path, String newName) {
        RenameDialog<Path> dialog = new RenameDialog<>(FileUtils.getFileName(path), newName, Files.isDirectory(path));
        dialog.setResultConverter(buttonType -> buttonType != null && !buttonType.getButtonData().isCancelButton() ?
                path.getParent().resolve(dialog.getNewName()) : null);
        return dialog;
    }

    public static RenameDialog<File> create(File file) {
        return create(file, file.getName());
    }

    public static RenameDialog<File> create(File file, String newName) {
        RenameDialog<File> dialog = new RenameDialog<>(file.getName(), newName, file.isDirectory());
        dialog.setResultConverter(buttonType -> buttonType != null && !buttonType.getButtonData().isCancelButton()
                ? new File(file.getParent(), dialog.getNewName()) : null);
        return dialog;
    }

    private final String rawName;
    private final boolean isDirectory;

    private final Label label;
    private final TextField editor;

    public RenameDialog(String rawName, String newName, boolean isDirectory) {
        super(ButtonType.OK, ButtonType.CANCEL);
        this.rawName = rawName;
        this.isDirectory = isDirectory;

        setTitle(AppL10n.localize("dialog.rename.title"));

        VBox vBox = new VBox(10);
        vBox.setMinSize(300, Region.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(10));

        label = new Label(AppL10n.localize(
                isDirectory ? "dialog.rename.message.directory" : "dialog.rename.message.file", rawName));
        editor = new TextField(newName);

        vBox.getChildren().addAll(label, editor, getButtonBar());

        setScene(new Scene(vBox));

        addEventFilter(WindowEvent.WINDOW_SHOWN, event -> Platform.runLater(this::focusAndSelect));
    }

    public String getRawName() {
        return rawName;
    }

    public String getNewName() {
        return editor.getText().trim();
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

    private void focusAndSelect() {
        editor.requestFocus();
        if (isDirectory) {
            editor.selectAll();
        } else {
            int dotIndex = editor.getText().indexOf('.');
            editor.selectRange(0, dotIndex != -1 ? dotIndex : editor.getLength());
        }
    }

    @Override
    protected void setResultAndClose(ButtonType buttonType, boolean close) {
        if (buttonType == null || buttonType.getButtonData().isCancelButton()) {
            super.setResultAndClose(buttonType, close);
        } else {
            String newName = getNewName();
            if (newName.isEmpty()) {
                showMessage(AppL10n.localize("dialog.rename.error.emptyFileName"));
            } else if (!FileUtils.validateFileName(newName)) {
                showMessage(AppL10n.localize("dialog.rename.error.invalidFileName"));
            } else {
                super.setResultAndClose(buttonType, close);
            }
        }
    }

    private Label popOverLabel;
    private PopOver popOver;

    protected void showMessage(String message) {
        if (popOver == null) {
            popOverLabel = new Label();
            popOverLabel.setPadding(new Insets(8));

            popOver = new PopOver();
            popOver.setAutoHide(true);
            popOver.setAnimated(false);
            popOver.setDetachable(false);
            popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
            popOver.setContentNode(popOverLabel);

            editor.textProperty().addListener(observable -> {
                if (popOver.isShowing()) popOver.hide();
            });
        }
        popOverLabel.setText(message);
        popOver.show(editor);

        focusAndSelect();
    }
}
