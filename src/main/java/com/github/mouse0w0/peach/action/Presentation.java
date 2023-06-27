package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.Icon;
import com.github.mouse0w0.peach.util.property.PropertyChangeListener;
import com.github.mouse0w0.peach.util.property.PropertyListenerHelper;
import com.github.mouse0w0.peach.util.property.PropertyObservable;
import org.jetbrains.annotations.NotNull;

public class Presentation implements PropertyObservable {
    public static final String TEXT_PROP = "text";
    public static final String DESCRIPTION_PROP = "description";
    public static final String ICON_PROP = "icon";
    public static final String DISABLE_PROP = "disable";
    public static final String VISIBLE_PROP = "visible";

    private PropertyListenerHelper helper;

    private String text;
    private String description;
    private Icon icon;
    private boolean disable = false;
    private boolean visible = true;

    public Presentation(Action action) {
        this.text = action.getText();
        this.description = action.getDescription();
        this.icon = action.getIcon();
    }

    public final String getText() {
        return text;
    }

    public final void setText(String value) {
        String oldValue = this.text;
        this.text = value;
        PropertyListenerHelper.firePropertyChange(helper, TEXT_PROP, oldValue, value);
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String value) {
        String oldValue = this.description;
        this.description = value;
        PropertyListenerHelper.firePropertyChange(helper, DESCRIPTION_PROP, oldValue, value);
    }

    public final Icon getIcon() {
        return icon;
    }

    public final void setIcon(Icon value) {
        Icon oldValue = this.icon;
        this.icon = value;
        PropertyListenerHelper.firePropertyChange(helper, ICON_PROP, oldValue, value);
    }

    public final boolean isDisable() {
        return disable;
    }

    public final void setDisable(boolean value) {
        boolean oldValue = this.disable;
        this.disable = value;
        PropertyListenerHelper.firePropertyChange(helper, DISABLE_PROP, oldValue, value);
    }

    public final boolean isVisible() {
        return visible;
    }

    public final void setVisible(boolean value) {
        boolean oldValue = this.visible;
        this.visible = value;
        PropertyListenerHelper.firePropertyChange(helper, VISIBLE_PROP, oldValue, value);
    }

    @Override
    public final void addListener(@NotNull PropertyChangeListener listener) {
        helper = PropertyListenerHelper.addListener(helper, this, listener);
    }

    @Override
    public final void removeListener(@NotNull PropertyChangeListener listener) {
        helper = PropertyListenerHelper.removeListener(helper, listener);
    }
}
