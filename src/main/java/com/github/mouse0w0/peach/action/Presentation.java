package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.Icon;

public interface Presentation {
    String getText();

    void setText(String value);

    String getDescription();

    void setDescription(String value);

    Icon getIcon();

    void setIcon(Icon value);

    boolean isDisable();

    void setDisable(boolean value);

    boolean isVisible();

    void setVisible(boolean value);

    default boolean isSelected() {
        throw new UnsupportedOperationException("selected");
    }

    default void setSelected(boolean value) {
        throw new UnsupportedOperationException("selected");
    }
}
