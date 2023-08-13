package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.FilePicker;
import com.sun.javafx.scene.control.FakeFocusTextField;
import javafx.scene.AccessibleRole;
import javafx.scene.Cursor;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class FilePickerSkin extends SkinBase<FilePicker> {
    private final FakeFocusTextField editor;
    private final StackPane clearButton;
    private final StackPane openButton;

    public FilePickerSkin(FilePicker control) {
        super(control);

        editor = new FakeFocusTextField();
        editor.getStyleClass().setAll("editor");
        editor.textProperty().bindBidirectional(control.valueProperty());
        editor.promptTextProperty().bind(control.promptTextProperty());
        editor.editableProperty().bind(control.editableProperty());
        editor.focusTraversableProperty().bind(control.editableProperty());
        control.focusedProperty().addListener(observable -> editor.setFakeFocus(control.isFocused()));
        control.addEventFilter(KeyEvent.ANY, e -> {
            if (control.isEditable()) {
                if (e.getTarget().equals(editor)) return;

                if (e.getCode() == KeyCode.ESCAPE) return;

                editor.fireEvent(e.copyFor(editor, editor));

                if (e.getCode() == KeyCode.ENTER) return;

                e.consume();
            }
        });

        Region clear = new Region();
        clear.getStyleClass().add("clear");

        clearButton = new StackPane(clear);
        clearButton.getStyleClass().add("clear-button");
        clearButton.setAccessibleRole(AccessibleRole.BUTTON);
        clearButton.setCursor(Cursor.HAND);
        clearButton.visibleProperty().bind(control.valueProperty().isNotEmpty());
        clearButton.setOnMousePressed(event -> {
            event.consume();
            getSkinnable().setValue("");
        });

        Region open = new Region();
        open.getStyleClass().add("open");

        openButton = new StackPane(open);
        openButton.getStyleClass().add("open-button");
        openButton.setAccessibleRole(AccessibleRole.BUTTON);
        openButton.setCursor(Cursor.HAND);
        openButton.setOnMousePressed(event -> {
            event.consume();
            getSkinnable().showDialog();
        });

        getChildren().addAll(editor, clearButton, openButton);
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double editorWidth = snapSizeX(editor.minWidth(-1));
        double clearButtonWidth = snapSizeX(clearButton.minWidth(-1));
        double openButtonWidth = snapSizeX(openButton.minWidth(-1));
        return leftInset + editorWidth + clearButtonWidth + openButtonWidth + rightInset;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double editorHeight = snapSizeY(editor.minHeight(-1));
        double clearButtonHeight = snapSizeY(clearButton.minHeight(-1));
        double openButtonHeight = snapSizeY(openButton.minHeight(-1));
        return topInset + Math.max(Math.max(editorHeight, clearButtonHeight), openButtonHeight) + bottomInset;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double editorWidth = snapSizeX(editor.prefWidth(-1));
        double clearButtonWidth = snapSizeX(clearButton.prefWidth(-1));
        double openButtonWidth = snapSizeX(openButton.prefWidth(-1));
        return leftInset + editorWidth + clearButtonWidth + openButtonWidth + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double editorHeight = snapSizeY(editor.prefHeight(-1));
        double clearButtonHeight = snapSizeY(clearButton.prefHeight(-1));
        double openButtonHeight = snapSizeY(openButton.prefHeight(-1));
        return topInset + Math.max(Math.max(editorHeight, clearButtonHeight), openButtonHeight) + bottomInset;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        double openButtonWidth = snapSizeX(openButton.prefWidth(-1));
        double clearButtonWidth = snapSizeX(clearButton.prefWidth(-1));
        double editorWidth = contentWidth - clearButtonWidth - openButtonWidth;
        editor.resizeRelocate(contentX, contentY, editorWidth, contentHeight);
        clearButton.resizeRelocate(contentX + editorWidth, contentY, clearButtonWidth, contentHeight);
        openButton.resizeRelocate(contentX + editorWidth + clearButtonWidth, contentY, openButtonWidth, contentHeight);
    }
}
