package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.Icon;
import org.jetbrains.annotations.NotNull;

public final class DummyPresentation implements Presentation {
    private String text;
    private String description;
    private Icon icon;
    private boolean disable;
    private boolean visible = true;
    private boolean selected;

    public DummyPresentation(@NotNull Action action) {
        this.text = action.getText();
        this.description = action.getDescription();
        this.icon = action.getIcon();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
