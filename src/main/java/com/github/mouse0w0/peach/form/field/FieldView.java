package com.github.mouse0w0.peach.form.field;

import javafx.beans.InvalidationListener;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class FieldView extends Region {
    private final Field field;

    private final Node label;
    private final Node editor;

    public FieldView(Field field, Node label, Node editor) {
        this.field = field;
        this.label = label;
        this.editor = editor;

        getStyleClass().add("form-item");

        visibleProperty().bind(field.visibleProperty());
        managedProperty().bind(field.visibleProperty());

        getChildren().addAll(label, editor);

        final InvalidationListener requestLayout = observable -> requestLayout();
        field.colSpanProperty().addListener(requestLayout);
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    @Override
    protected double computePrefWidth(double height) {
        return snappedLeftInset() + label.prefWidth(height) + editor.prefWidth(height) + snappedRightInset();
    }

    @Override
    protected double computePrefHeight(double width) {
        final double left = snappedLeftInset();
        final double right = snappedRightInset();
        final double top = snappedTopInset();
        final double bottom = snappedBottomInset();
        final double contentWidth = width - left - right;
        final double cellWidth = contentWidth / field.getColSpan().getSpan();
        final double labelWidth = cellWidth * 2;
        return top + Math.max(label.prefHeight(labelWidth), editor.prefHeight(contentWidth - labelWidth)) + bottom;
    }

    @Override
    protected void layoutChildren() {
        final double left = snappedLeftInset();
        final double right = snappedRightInset();
        final double top = snappedTopInset();
        final double bottom = snappedBottomInset();
        final double width = getWidth();
        final double height = getHeight();
        final double contentWidth = width - left - right;
        final double contentHeight = height - top - bottom;
        final double cellWidth = contentWidth / field.getColSpan().getSpan();
        final double labelWidth = cellWidth * 2;
        layoutInArea(label, left, top, labelWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
        layoutInArea(editor, left + labelWidth, top, contentWidth - labelWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
    }
}
