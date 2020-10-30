package com.github.mouse0w0.peach.action;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class Appearance {

    private StringProperty text;
    private StringProperty icon;
    private String description;

    private BooleanProperty disable;
    private BooleanProperty visible;

    public StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(this, "text");
        }
        return text;
    }

    public String getText() {
        return text != null ? text.get() : null;
    }

    public void setText(String text) {
        textProperty().set(text);
    }

    public StringProperty iconProperty() {
        if (icon == null) {
            icon = new SimpleStringProperty(this, "icon");
        }
        return icon;
    }

    public String getIcon() {
        return icon != null ? icon.get() : null;
    }

    public void setIcon(String icon) {
        iconProperty().set(icon);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BooleanProperty disableProperty() {
        if (disable == null) {
            disable = new SimpleBooleanProperty(this, "disable", false);
        }
        return disable;
    }

    public boolean isDisable() {
        return disable != null && disable.get();
    }

    public void setDisable(boolean disable) {
        disableProperty().set(disable);
    }

    public BooleanProperty visibleProperty() {
        if (visible == null) {
            visible = new SimpleBooleanProperty(this, "visible", true);
        }
        return visible;
    }

    public boolean isVisible() {
        return visible == null || visible.get();
    }

    public void setVisible(boolean visible) {
        visibleProperty().set(visible);
    }
}
