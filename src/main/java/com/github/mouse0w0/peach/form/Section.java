package com.github.mouse0w0.peach.form;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class Section extends Group {

    private StringProperty text;
    private BooleanProperty collapsible;
    private BooleanProperty expanded;

    public Section() {
        getStyleClass().setAll("form-section", "titled-pane");
    }

    public final StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(this, "text");
        }
        return text;
    }

    public final String getText() {
        return text != null ? text.get() : null;
    }

    public final void setText(String text) {
        textProperty().set(text);
    }

    public final BooleanProperty collapsibleProperty() {
        if (collapsible == null) {
            collapsible = new SimpleBooleanProperty(this, "collapsible", false);
        }
        return collapsible;
    }

    public final boolean isCollapsible() {
        return collapsible != null && collapsible.get();
    }

    public final void setCollapsible(boolean collapsible) {
        collapsibleProperty().set(collapsible);
    }

    public final BooleanProperty expandedProperty() {
        if (expanded == null) {
            expanded = new SimpleBooleanProperty(this, "expanded", true);
        }
        return expanded;
    }

    public final boolean isExpanded() {
        return expanded == null || expanded.get();
    }

    public final void setExpanded(boolean expanded) {
        expandedProperty().set(expanded);
    }

    @Override
    protected Node createNode() {
        TitledPane titledPane = new TitledPane();
        titledPane.setFocusTraversable(false);
        titledPane.setMaxWidth(Double.MAX_VALUE);
        titledPane.textProperty().bind(textProperty());
        titledPane.collapsibleProperty().bind(collapsibleProperty());
        titledPane.expandedProperty().bindBidirectional(expandedProperty());
        titledPane.visibleProperty().bind(visibleProperty());
        titledPane.managedProperty().bind(visibleProperty());
        titledPane.idProperty().bind(idProperty());
        Bindings.bindContent(titledPane.getStyleClass(), getStyleClass());

        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid");
        gridPane.getColumnConstraints().setAll(Utils.COLUMN_CONSTRAINTS);
        titledPane.setContent(gridPane);

        getElements().addListener((ListChangeListener<? super Element>) observable -> {
            Utils.layoutElements(gridPane, getElements());
        });
        Utils.layoutElements(gridPane, getElements());
        return titledPane;
    }
}
