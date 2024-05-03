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

    public void setText(String value) {
        this.text = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon value) {
        this.icon = value;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean value) {
        this.disable = value;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean value) {
        this.visible = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean value) {
        this.selected = value;
    }
}
