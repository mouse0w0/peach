package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.Icon;
import org.jetbrains.annotations.NotNull;

public final class Presentation {
    public static final String TEXT_PROP = "text";
    public static final String DESCRIPTION_PROP = "description";
    public static final String ICON_PROP = "icon";
    public static final String DISABLE_PROP = "disable";
    public static final String VISIBLE_PROP = "visible";

    private final ChangeListener changeListener;

    private String text;
    private String description;
    private Icon icon;
    private boolean disable = false;
    private boolean visible = true;

    public Presentation(@NotNull Action action, ChangeListener changeListener) {
        this.changeListener = changeListener;
        this.text = action.getText();
        this.description = action.getDescription();
        this.icon = action.getIcon();
    }

    public String getText() {
        return text;
    }

    public void setText(String value) {
        String oldValue = this.text;
        this.text = value;
        notifyChanged(TEXT_PROP, oldValue, value);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        String oldValue = this.description;
        this.description = value;
        notifyChanged(DESCRIPTION_PROP, oldValue, value);
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon value) {
        Icon oldValue = this.icon;
        this.icon = value;
        notifyChanged(ICON_PROP, oldValue, value);
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean value) {
        boolean oldValue = this.disable;
        this.disable = value;
        notifyChanged(DISABLE_PROP, oldValue, value);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean value) {
        boolean oldValue = this.visible;
        this.visible = value;
        notifyChanged(VISIBLE_PROP, oldValue, value);
    }

    private void notifyChanged(String propertyName, Object oldValue, Object newValue) {
        if (changeListener != null) {
            changeListener.propertyChanged(propertyName, oldValue, newValue);
        }
    }

    public interface ChangeListener {
        void propertyChanged(String propertyName, Object oldValue, Object newValue);
    }
}
