package com.github.mouse0w0.peach.form.skin;

import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.form.LabelPosition;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Region;

class ElementView extends Region {
    private final Element element;

    private final Node label;
    private final Node editor;

    ElementView(Element element) {
        this.element = element;

        visibleProperty().bind(element.visibleProperty());
        managedProperty().bind(element.visibleProperty());
        idProperty().bind(element.idProperty());
        Bindings.bindContent(getStyleClass(), element.getStyleClass());

        label = element.getLabel();
        editor = element.getEditor();

        getChildren().addAll(label, editor);
    }

    public Element getElement() {
        return element;
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    @Override
    protected double computePrefWidth(double height) {
        final LabelPosition labelPosition = element.getLabelPosition();
        if (labelPosition == LabelPosition.TOP) {
            return snappedLeftInset() + Math.max(label.prefWidth(-1), editor.prefWidth(-1)) + snappedRightInset();
        } else if (labelPosition == LabelPosition.NONE) {
            return snappedLeftInset() + editor.prefWidth(-1) + snappedRightInset();
        } else {
            return snappedLeftInset() + label.prefWidth(-1) + editor.prefWidth(-1) + snappedRightInset();
        }
    }

    @Override
    protected double computePrefHeight(double width) {
        final double left = snappedLeftInset();
        final double right = snappedRightInset();
        final double top = snappedTopInset();
        final double bottom = snappedBottomInset();
        final double contentWidth = width - left - right;
        final LabelPosition labelPosition = element.getLabelPosition();
        if (labelPosition == LabelPosition.TOP) {
            return top + label.prefHeight(contentWidth) + editor.prefHeight(contentWidth) + bottom;
        } else if (labelPosition == LabelPosition.NONE) {
            return top + editor.prefHeight(contentWidth) + bottom;
        } else {
            final double cellWidth = contentWidth / element.getColSpan().getSpan();
            final double labelWidth = cellWidth * 2;
            return top + Math.max(label.prefHeight(labelWidth), editor.prefHeight(contentWidth - labelWidth)) + bottom;
        }
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
        final LabelPosition labelPosition = element.getLabelPosition();
        if (labelPosition == LabelPosition.TOP) {
            final double labelHeight = label.prefHeight(-1);
            layoutInArea(label, left, top, contentWidth, labelHeight, 0, HPos.LEFT, VPos.TOP);
            layoutInArea(editor, left, top + labelHeight, contentWidth, contentHeight - labelHeight, 0, HPos.LEFT, VPos.TOP);
        } else if (labelPosition == LabelPosition.NONE) {
            layoutInArea(editor, left, top, contentWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
        } else {
            final double cellWidth = contentWidth / element.getColSpan().getSpan();
            final double labelWidth = cellWidth * 2;
            layoutInArea(label, left, top, labelWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
            layoutInArea(editor, left + labelWidth, top, contentWidth - labelWidth, contentHeight, 0, HPos.LEFT, VPos.TOP);
        }
    }
}
