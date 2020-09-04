package com.github.mouse0w0.peach.action;

import javafx.scene.image.Image;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class Appearance {

    public static final String PROP_TEXT = "text";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_ICON = "icon";
    public static final String PROP_DISABLE = "disable";
    public static final String PROP_VISIBLE = "visible";

    private String text;
    private String description;
    private Image icon;

    private boolean disable = false;
    private boolean visible = true;

    private PropertyChangeSupport changeSupport;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        String oldValue = this.text;
        this.text = text;
        firePropertyChange(PROP_TEXT, oldValue, text);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String oldValue = this.description;
        this.description = description;
        firePropertyChange(PROP_DESCRIPTION, oldValue, description);
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        Image oldValue = this.icon;
        this.icon = icon;
        firePropertyChange(PROP_ICON, oldValue, icon);
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        boolean oldValue = this.disable;
        this.disable = disable;
        firePropertyChange(PROP_DISABLE, oldValue, disable);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        boolean oldValue = this.visible;
        this.visible = visible;
        firePropertyChange(PROP_VISIBLE, oldValue, visible);
    }

    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (changeSupport == null || oldValue == newValue) return;
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    private void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        if (changeSupport == null || oldValue == newValue) return;
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
