package com.github.mouse0w0.peach.form;

import com.github.mouse0w0.peach.form.skin.SectionView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public class Section extends Group {

    private StringProperty text;
    private BooleanProperty collapsible;
    private BooleanProperty expanded;

    public Section() {
        getStyleClass().setAll("form-section");
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
    protected Node createDefaultNode() {
        return new SectionView(this);
    }
}
