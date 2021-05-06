package com.github.mouse0w0.peach.form.skin;

import com.github.mouse0w0.peach.form.DisplayMode;
import com.github.mouse0w0.peach.form.Element;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

class ElementSkin extends Region {
    private final Element element;

    private final Label label;
    private final Node editor;

    ElementSkin(Element element) {
        this.element = element;

        visibleProperty().bind(element.visibleProperty());
        managedProperty().bind(element.visibleProperty());
        idProperty().bind(element.idProperty());
        Bindings.bindContent(getStyleClass(), element.getStyleClass());

        label = new Label();
        label.setWrapText(true);
        label.textProperty().bind(element.textProperty());

        editor = element.getEditor();

        getChildren().addAll(label, editor);

        element.displayModeProperty().addListener(observable -> updateDisplayMode());
        updateDisplayMode();
    }

    public Element getElement() {
        return element;
    }

    private void updateDisplayMode() {
        boolean labelVisible = element.getDisplayMode() != DisplayMode.EDITOR_ONLY;
        label.setVisible(labelVisible);
        label.setManaged(labelVisible);
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    @Override
    protected double computePrefWidth(double height) {
        final DisplayMode displayMode = element.getDisplayMode();
        if (displayMode == DisplayMode.VERTICAL) {
            return snappedLeftInset() + Math.max(label.prefWidth(-1), editor.prefWidth(-1)) + snappedRightInset();
        } else if (displayMode == DisplayMode.EDITOR_ONLY) {
            return snappedLeftInset() + editor.prefWidth(-1) + snappedRightInset();
        } else {
            return snappedLeftInset() + label.prefWidth(-1) + editor.prefWidth(-1) + snappedRightInset();
        }
    }

    @Override
    protected double computePrefHeight(double width) {
        final double leftInset = snappedLeftInset();
        final double rightInset = snappedRightInset();
        final double topInset = snappedTopInset();
        final double bottomInset = snappedBottomInset();
        final double contentWidth = width - leftInset - rightInset;
        final DisplayMode displayMode = element.getDisplayMode();
        if (displayMode == DisplayMode.VERTICAL) {
            return topInset + label.prefHeight(contentWidth) + editor.prefHeight(contentWidth) + bottomInset;
        } else if (displayMode == DisplayMode.EDITOR_ONLY) {
            return topInset + editor.prefHeight(contentWidth) + bottomInset;
        } else {
            final double cellWidth = contentWidth / element.getColSpan().getSpan();
            final double labelWidth = cellWidth * 2;
            return topInset + Math.max(label.prefHeight(labelWidth), editor.prefHeight(contentWidth - labelWidth)) + bottomInset;
        }
    }

    @Override
    protected void layoutChildren() {
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double width = getWidth();
        final double height = getHeight();
        final double contentWidth = width - x - snappedRightInset();
        final double contentHeight = height - y - snappedBottomInset();
        final DisplayMode displayMode = element.getDisplayMode();
        if (displayMode == DisplayMode.VERTICAL) {
            final double labelHeight = label.prefHeight(-1);
            layoutInArea(label, x, y, contentWidth, labelHeight, 0, HPos.LEFT, VPos.TOP);
            layoutInArea(editor, x, y + labelHeight, contentWidth, contentHeight - labelHeight, 0, HPos.LEFT, VPos.TOP);
        } else if (displayMode == DisplayMode.EDITOR_ONLY) {
            layoutInArea(editor, x, y, contentWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
        } else {
            final double cellWidth = contentWidth / element.getColSpan().getSpan();
            final double labelWidth = cellWidth * 2;
            layoutInArea(label, x, y, labelWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
            layoutInArea(editor, x + labelWidth, y, contentWidth - labelWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
        }
    }
}
