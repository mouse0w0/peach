package com.github.mouse0w0.peach.action;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public final class Appearance {

    private StringProperty text;
    private StringProperty description;
    private ObjectProperty<Image> icon;

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

    public StringProperty descriptionProperty() {
        if (description == null) {
            description = new SimpleStringProperty(this, "description");
        }
        return description;
    }

    public String getDescription() {
        return description != null ? description.get() : null;
    }

    public void setDescription(String description) {
        descriptionProperty().set(description);
    }

    public ObjectProperty<Image> iconProperty() {
        if (icon == null) {
            icon = new SimpleObjectProperty<>(this, "icon");
        }
        return icon;
    }

    public Image getIcon() {
        return icon != null ? icon.get() : null;
    }

    public void setIcon(Image icon) {
        iconProperty().set(icon);
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
