package com.github.mouse0w0.peach.ui.layout;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.Arrays;
import java.util.List;

public class Form extends Region {
    private static final int COLUMN_COUNT = 12;
    private static final ColumnConstraints[] COLUMN_CONSTRAINTS;

    static {
        ColumnConstraints colConst = new ColumnConstraints();
        colConst.setPercentWidth(100D / COLUMN_COUNT);
        ColumnConstraints[] colConstArray = new ColumnConstraints[COLUMN_COUNT];
        Arrays.fill(colConstArray, colConst);
        COLUMN_CONSTRAINTS = colConstArray;
    }

    private final GridPane gridPane;

    private final InvalidationListener spanInvalidationListener = observable -> {
        needLayoutItems = true;
        requestLayout();
    };
    private final InvalidationListener spanWeakInvalidationListener = new WeakInvalidationListener(spanInvalidationListener);

    public static Form form() {
        return new Form();
    }

    public static Form form(FormItem... items) {
        return new Form(items);
    }

    public Form() {
        getStyleClass().add("form");

        gridPane = new GridPane();
        gridPane.getStyleClass().add("grid");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.getColumnConstraints().addAll(COLUMN_CONSTRAINTS);
        getChildren().add(gridPane);

        Bindings.bindContent(gridPane.getChildren(), items);
        items.addListener((ListChangeListener<? super FormItem>) c -> {
            needLayoutItems = true;
            while (c.next()) {
                if (c.wasAdded()) {
                    for (int i = c.getFrom(), to = c.getTo(); i < to; i++) {
                        FormItem item = items.get(i);
                        item.spanProperty().addListener(spanWeakInvalidationListener);
                    }
                }
                if (c.wasRemoved()) {
                    List<? extends FormItem> removed = c.getRemoved();
                    for (int i = 0; i < removed.size(); i++) {
                        FormItem item = removed.get(i);
                        item.spanProperty().removeListener(spanWeakInvalidationListener);
                    }
                }
            }
        });
    }

    public Form(FormItem... items) {
        this();
        getItems().addAll(items);
    }

    private final ObservableList<FormItem> items = FXCollections.observableArrayList();

    public ObservableList<FormItem> getItems() {
        return items;
    }

    private boolean needLayoutItems = true;

    private void layoutItems() {
        if (needLayoutItems) {
            int colOffset = 0, rowOffset = 0;
            for (FormItem item : items) {
                if (!item.isManaged()) {
                    continue;
                }
                int span = Math.min(Math.max(1, item.getSpan()), 12);
                if (colOffset + span > COLUMN_COUNT) {
                    colOffset = 0;
                    rowOffset++;
                }
                GridPane.setConstraints(item, colOffset, rowOffset, span, 1);
                colOffset += span;
            }
            needLayoutItems = false;
        }
    }

    @Override
    protected double computeMinWidth(double height) {
        layoutItems();
        return snappedLeftInset() + snapSizeX(gridPane.minWidth(-1)) + snappedRightInset();
    }

    @Override
    protected double computeMinHeight(double width) {
        layoutItems();
        return snappedTopInset() + snapSizeY(gridPane.minHeight(-1)) + snappedBottomInset();
    }

    @Override
    protected double computePrefWidth(double height) {
        layoutItems();
        return snappedLeftInset() + snapSizeX(gridPane.prefWidth(-1)) + snappedRightInset();
    }

    @Override
    protected double computePrefHeight(double width) {
        layoutItems();
        return snappedTopInset() + snapSizeY(gridPane.prefHeight(-1)) + snappedBottomInset();
    }

    @Override
    protected double computeMaxWidth(double height) {
        layoutItems();
        return snappedLeftInset() + snapSizeX(gridPane.maxWidth(-1)) + snappedRightInset();
    }

    @Override
    protected double computeMaxHeight(double width) {
        layoutItems();
        return snappedTopInset() + snapSizeY(gridPane.maxHeight(-1)) + snappedBottomInset();
    }

    @Override
    public double getBaselineOffset() {
        layoutItems();
        return snappedTopInset() + gridPane.getBaselineOffset();
    }

    @Override
    protected void layoutChildren() {
        layoutItems();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSizeX(getWidth()) - x - snappedRightInset();
        final double h = snapSizeY(getHeight()) - y - snappedBottomInset();
        gridPane.resizeRelocate(x, y, w, h);
    }
}
