package com.github.mouse0w0.peach.ui.layout;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class FormItem extends Region {
    public static final int ONE = 12;
    public static final int FIVE_SIXTH = 10;
    public static final int TWO_THIRD = 8;
    public static final int HALF = 6;
    public static final int THIRD = 4;
    public static final int QUARTER = 3;
    public static final int SIXTH = 2;
    public static final int TWELFTH = 1;

    public static FormItem one(String label, Node field) {
        return new FormItem(label, field);
    }

    public static FormItem one(Node label, Node field) {
        return new FormItem(label, field);
    }

    public static FormItem one(Node field) {
        return new FormItem(field);
    }

    public static FormItem one() {
        return new FormItem();
    }

    public static FormItem fiveSixth(String label, Node field) {
        return new FormItem(FIVE_SIXTH, label, field);
    }

    public static FormItem fiveSixth(Node label, Node field) {
        return new FormItem(FIVE_SIXTH, label, field);
    }

    public static FormItem fiveSixth(Node field) {
        return new FormItem(FIVE_SIXTH, field);
    }

    public static FormItem fiveSixth() {
        return new FormItem(FIVE_SIXTH);
    }

    public static FormItem twoThird(String label, Node field) {
        return new FormItem(TWO_THIRD, label, field);
    }

    public static FormItem twoThird(Node label, Node field) {
        return new FormItem(TWO_THIRD, label, field);
    }

    public static FormItem twoThird(Node field) {
        return new FormItem(TWO_THIRD, field);
    }

    public static FormItem twoThird() {
        return new FormItem(TWO_THIRD);
    }

    public static FormItem half(String label, Node field) {
        return new FormItem(HALF, label, field);
    }

    public static FormItem half(Node label, Node field) {
        return new FormItem(HALF, label, field);
    }

    public static FormItem half(Node field) {
        return new FormItem(HALF, field);
    }

    public static FormItem half() {
        return new FormItem(HALF);
    }

    public static FormItem third(String label, Node field) {
        return new FormItem(THIRD, label, field);
    }

    public static FormItem third(Node label, Node field) {
        return new FormItem(THIRD, label, field);
    }

    public static FormItem third(Node field) {
        return new FormItem(THIRD, field);
    }

    public static FormItem third() {
        return new FormItem(THIRD);
    }

    public static FormItem quarter(String label, Node field) {
        return new FormItem(QUARTER, label, field);
    }

    public static FormItem quarter(Node label, Node field) {
        return new FormItem(QUARTER, label, field);
    }

    public static FormItem quarter(Node field) {
        return new FormItem(QUARTER, field);
    }

    public static FormItem quarter() {
        return new FormItem(QUARTER);
    }

    public static FormItem sixth(String label, Node field) {
        return new FormItem(SIXTH, label, field);
    }

    public static FormItem sixth(Node label, Node field) {
        return new FormItem(SIXTH, label, field);
    }

    public static FormItem sixth(Node field) {
        return new FormItem(SIXTH, field);
    }

    public static FormItem sixth() {
        return new FormItem(SIXTH);
    }

    public static FormItem twelfth(String label, Node field) {
        return new FormItem(TWELFTH, label, field);
    }

    public static FormItem twelfth(Node label, Node field) {
        return new FormItem(TWELFTH, label, field);
    }

    public static FormItem twelfth(Node field) {
        return new FormItem(TWELFTH, field);
    }

    public static FormItem twelfth() {
        return new FormItem(TWELFTH);
    }

    public FormItem() {
        getStyleClass().add("form-item");
    }

    public FormItem(int span) {
        this();
        setSpan(span);
    }

    public FormItem(Node field) {
        this();
        setField(field);
    }

    public FormItem(int span, Node field) {
        this();
        setSpan(span);
        setField(field);
    }

    public FormItem(String label, Node field) {
        this();
        setLabel(new Label(label));
        setField(field);
    }

    public FormItem(int span, String label, Node field) {
        this();
        setSpan(span);
        setLabel(new Label(label));
        setField(field);
    }

    public FormItem(Node label, Node field) {
        this();
        setLabel(label);
        setField(field);
    }

    public FormItem(int span, Node label, Node field) {
        this();
        setSpan(span);
        setLabel(label);
        setField(field);
    }

    public final FormItem id(String id) {
        setId(id);
        return this;
    }

    public final FormItem styleClass(String styleClass) {
        getStyleClass().add(styleClass);
        return this;
    }

    public final FormItem styleClass(String... styleClasses) {
        getStyleClass().addAll(styleClasses);
        return this;
    }

    public final FormItem visible(boolean value) {
        setVisible(value);
        return this;
    }

    public final FormItem bindVisible(ObservableBooleanValue observable) {
        visibleProperty().bind(observable);
        return this;
    }

    public final FormItem managed(boolean value) {
        setManaged(value);
        return this;
    }

    public final FormItem bindManaged(ObservableBooleanValue observable) {
        managedProperty().bind(observable);
        return this;
    }

    private ObjectProperty<Node> label;

    public final ObjectProperty<Node> labelProperty() {
        if (label == null) {
            label = new NodeProperty("label");
        }
        return label;
    }

    public final Node getLabel() {
        return label != null ? label.get() : null;
    }

    public final void setLabel(Node label) {
        labelProperty().set(label);
    }

    private ObjectProperty<Node> field;

    public final ObjectProperty<Node> fieldProperty() {
        if (field == null) {
            field = new NodeProperty("field");
        }
        return field;
    }

    public final Node getField() {
        return field != null ? field.get() : null;
    }

    public final void setField(Node field) {
        fieldProperty().set(field);
    }

    private IntegerProperty span;

    public final IntegerProperty spanProperty() {
        if (span == null) {
            span = new SimpleIntegerProperty(this, "span", ONE) {
                @Override
                protected void invalidated() {
                    requestLayout();
                }
            };
        }
        return span;
    }

    public final int getSpan() {
        return span != null ? span.get() : ONE;
    }

    public final void setSpan(int span) {
        spanProperty().set(span);
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    @Override
    protected double computeMinWidth(double height) {
        final Node label = getLabel(), field = getField();
        final double labelWidth = label != null ? snapSizeX(label.minWidth(-1)) : 0;
        final double fieldWidth = field != null ? snapSizeX(field.minWidth(-1)) : 0;
        return snappedLeftInset() + labelWidth + fieldWidth + snappedRightInset();
    }

    @Override
    protected double computeMinHeight(double width) {
        final double contentWidth = width - snappedLeftInset() - snappedRightInset();
        final double cellWidth = contentWidth / getSpan();
        final Node label = getLabel(), field = getField();
        final double labelWidth = label != null ? cellWidth * 2 : 0;
        final double fieldWidth = contentWidth - labelWidth;
        final double labelHeight = label != null ? snapSizeY(label.minHeight(labelWidth)) : 0;
        final double fieldHeight = field != null ? snapSizeY(field.minHeight(fieldWidth)) : 0;
        return snappedTopInset() + Math.max(labelHeight, fieldHeight) + snappedBottomInset();
    }

    @Override
    protected double computePrefWidth(double height) {
        final Node label = getLabel(), field = getField();
        final double labelWidth = label != null ? snapSizeX(label.prefWidth(-1)) : 0;
        final double fieldWidth = field != null ? snapSizeX(field.prefWidth(-1)) : 0;
        return snappedLeftInset() + labelWidth + fieldWidth + snappedRightInset();
    }

    @Override
    protected double computePrefHeight(double width) {
        final double contentWidth = width - snappedLeftInset() - snappedRightInset();
        final double cellWidth = contentWidth / getSpan();
        final Node label = getLabel(), field = getField();
        final double labelWidth = label != null ? cellWidth * 2 : 0;
        final double fieldWidth = contentWidth - labelWidth;
        final double labelHeight = label != null ? snapSizeY(label.prefHeight(labelWidth)) : 0;
        final double fieldHeight = field != null ? snapSizeY(field.prefHeight(fieldWidth)) : 0;
        return snappedTopInset() + Math.max(labelHeight, fieldHeight) + snappedBottomInset();
    }

    @Override
    protected void layoutChildren() {
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSizeX(getWidth()) - x - snappedRightInset();
        final double h = snapSizeY(getHeight()) - y - snappedBottomInset();
        final Node label = getLabel(), field = getField();
        final double cellWidth = w / getSpan();
        final double labelWidth = snapSizeX(label != null ? cellWidth * 2 : 0);
        final double fieldWidth = w - labelWidth;
        if (label != null) {
            layoutInArea(label, x, y, labelWidth, h, 0, HPos.LEFT, VPos.CENTER);
        }
        if (field != null) {
            layoutInArea(field, x + labelWidth, y, fieldWidth, h, 0, HPos.LEFT, VPos.CENTER);
        }
    }

    private final class NodeProperty extends ObjectPropertyBase<Node> {
        private final String name;

        private NodeProperty(String name) {
            this.name = name;
        }

        @Override
        public void set(Node newValue) {
            Node old = get();
            if (old != null) {
                getChildren().remove(old);
            }
            super.set(newValue);
            if (newValue != null) {
                getChildren().add(newValue);
            }
        }

        @Override
        public Object getBean() {
            return FormItem.this;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
