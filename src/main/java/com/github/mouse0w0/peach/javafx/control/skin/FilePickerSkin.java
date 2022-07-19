package com.github.mouse0w0.peach.javafx.control.skin;

import com.github.mouse0w0.peach.javafx.control.FilePicker;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleRole;
import javafx.scene.Cursor;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class FilePickerSkin extends SkinBase<FilePicker> {
    private static final PseudoClass FOCUSED = PseudoClass.getPseudoClass("focused");

    private final TextField editor;
    private final StackPane clearBtn;
    private final StackPane openChooserBtn;

    public FilePickerSkin(FilePicker filePicker) {
        super(filePicker);
        consumeMouseEvents(false);

        editor = new TextField();
        editor.getStyleClass().setAll("editor");
        editor.textProperty().bindBidirectional(filePicker.valueProperty());
        editor.promptTextProperty().bind(filePicker.promptTextProperty());
        editor.editableProperty().bind(filePicker.editableProperty());
        editor.focusedProperty().addListener(observable ->
                pseudoClassStateChanged(FOCUSED, editor.isFocused()));

        StackPane clearIcon = new StackPane();
        clearIcon.getStyleClass().setAll("clear-icon");

        clearBtn = new StackPane(clearIcon);
        clearBtn.getStyleClass().setAll("clear-button");
        clearBtn.setAccessibleRole(AccessibleRole.BUTTON);
        clearBtn.setCursor(Cursor.HAND);
        clearBtn.visibleProperty().bind(filePicker.valueProperty().isNotEmpty());
        clearBtn.setOnMousePressed(event -> {
            event.consume();
            getSkinnable().setValue("");
        });

        StackPane openChooserIcon = new StackPane();
        openChooserIcon.getStyleClass().setAll("open-icon");

        openChooserBtn = new StackPane(openChooserIcon);
        openChooserBtn.getStyleClass().setAll("open-button");
        openChooserBtn.setAccessibleRole(AccessibleRole.BUTTON);
        openChooserBtn.setCursor(Cursor.HAND);
        openChooserBtn.setOnMousePressed(event -> {
            event.consume();
            getSkinnable().showDialog();
        });

        getChildren().addAll(editor, clearBtn, openChooserBtn);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double editorWidth = snapSizeX(editor.prefWidth(-1));
        final double clearBtnWidth = snapSizeX(clearBtn.prefWidth(-1));
        final double openChooserBtnWidth = snapSizeX(openChooserBtn.prefWidth(-1));
        return leftInset + editorWidth + clearBtnWidth + openChooserBtnWidth + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final double editorHeight = snapSizeY(editor.prefHeight(-1));
        final double clearBtnHeight = snapSizeY(clearBtn.prefHeight(-1));
        final double openChooserBtnHeight = snapSizeY(openChooserBtn.prefHeight(-1));
        return topInset + Math.max(Math.max(editorHeight, clearBtnHeight), openChooserBtnHeight) + bottomInset;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefHeight(width);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        final double openChooserBtnWidth = snapSizeX(openChooserBtn.prefWidth(-1));
        final double clearBtnWidth = snapSizeX(clearBtn.prefWidth(-1));
        final double editorWidth = contentWidth - clearBtnWidth - openChooserBtnWidth;
        layoutInArea(editor, contentX, contentY, editorWidth, contentHeight, 0, HPos.LEFT, VPos.CENTER);
        layoutInArea(clearBtn, contentX + editorWidth, contentY,
                clearBtnWidth, contentHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(openChooserBtn, contentX + editorWidth + clearBtnWidth, contentY,
                openChooserBtnWidth, contentHeight, 0, HPos.CENTER, VPos.CENTER);
    }
}
