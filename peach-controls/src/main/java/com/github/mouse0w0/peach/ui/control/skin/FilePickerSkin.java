package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.FilePicker;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleRole;
import javafx.scene.Cursor;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class FilePickerSkin extends SkinBase<FilePicker> {
    private static final PseudoClass FOCUSED = PseudoClass.getPseudoClass("focused");

    private final TextField editor;
    private final StackPane openChooserBtn;

    public FilePickerSkin(FilePicker filePicker) {
        super(filePicker);
        consumeMouseEvents(false);

        editor = new TextField();
        editor.getStyleClass().setAll("editor");
        editor.textProperty().bindBidirectional(filePicker.textProperty());
        editor.promptTextProperty().bind(filePicker.promptTextProperty());
        editor.editableProperty().bind(filePicker.editableProperty());
        editor.focusedProperty().addListener(observable ->
                pseudoClassStateChanged(FOCUSED, editor.isFocused()));

        StackPane openChooserIcon = new StackPane();
        openChooserIcon.getStyleClass().setAll("open-chooser-icon");

        openChooserBtn = new StackPane(openChooserIcon);
        openChooserBtn.getStyleClass().setAll("open-chooser-button");
        openChooserBtn.setAccessibleRole(AccessibleRole.BUTTON);
        openChooserBtn.setCursor(Cursor.HAND);
        openChooserBtn.setOnMousePressed(event -> {
            event.consume();
            showDialog();
        });

        getChildren().addAll(editor, openChooserBtn);
    }

    protected void showDialog() {
        FilePicker filePicker = getSkinnable();
        FilePicker.Type type = filePicker.getType();
        File oldFile = filePicker.toFile();
        Window owner = filePicker.getScene().getWindow();
        File file = null;
        if (type == FilePicker.Type.OPEN_DIRECTORY) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle(filePicker.getTitle());
            if (oldFile == null) {
                directoryChooser.setInitialDirectory(filePicker.getInitialDirectory());
            } else {
                directoryChooser.setInitialDirectory(oldFile.getParentFile());
            }
            file = directoryChooser.showDialog(owner);
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(filePicker.getTitle());
            if (oldFile == null) {
                fileChooser.setInitialDirectory(filePicker.getInitialDirectory());
                fileChooser.setInitialFileName(filePicker.getInitialFileName());
            } else {
                fileChooser.setInitialDirectory(oldFile.getParentFile());
                fileChooser.setInitialFileName(oldFile.getName());
            }
            fileChooser.setSelectedExtensionFilter(filePicker.getSelectedExtensionFilter());
            fileChooser.getExtensionFilters().setAll(filePicker.getExtensionFilters());

            if (type == FilePicker.Type.OPEN_FILE) {
                file = fileChooser.showOpenDialog(owner);
            } else if (type == FilePicker.Type.SAVE_FILE) {
                file = fileChooser.showSaveDialog(owner);
            }
        }

        if (file != null) {
            filePicker.setFile(file);
        }
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double editorWidth = snapSize(editor.prefWidth(-1));
        final double openChooserBtnWidth = snapSize(openChooserBtn.prefWidth(-1));
        return leftInset + editorWidth + openChooserBtnWidth + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double editorHeight = snapSize(editor.prefHeight(-1));
        final double openChooserBtnHeight = snapSize(openChooserBtn.prefHeight(-1));
        return topInset + Math.max(editorHeight, openChooserBtnHeight) + bottomInset;
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double editorWidth = snapSize(editor.minWidth(-1));
        final double openChooserBtnWidth = snapSize(openChooserBtn.minWidth(-1));
        return leftInset + editorWidth + openChooserBtnWidth + rightInset;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double editorHeight = snapSize(editor.minHeight(-1));
        final double openChooserBtnHeight = snapSize(openChooserBtn.minHeight(-1));
        return topInset + Math.max(editorHeight, openChooserBtnHeight) + bottomInset;
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double editorWidth = snapSize(editor.maxWidth(-1));
        final double openChooserBtnWidth = snapSize(openChooserBtn.maxWidth(-1));
        return leftInset + editorWidth + openChooserBtnWidth + rightInset;
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double editorHeight = snapSize(editor.maxHeight(-1));
        final double openChooserBtnHeight = snapSize(openChooserBtn.maxHeight(-1));
        return topInset + Math.min(editorHeight, openChooserBtnHeight) + bottomInset;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        final double openChooserBtnWidth = snapSize(openChooserBtn.prefWidth(-1));
        final double editorWidth = contentWidth - openChooserBtnWidth;
        layoutInArea(editor, contentX, contentY, editorWidth, contentHeight, 0, HPos.LEFT, VPos.CENTER);
        layoutInArea(openChooserBtn, contentX + editorWidth, contentY,
                openChooserBtnWidth, contentHeight, 0, HPos.CENTER, VPos.CENTER);
    }
}
