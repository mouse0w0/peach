package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.Icon;

public interface Presentation {
    String getText();

    void setText(String text);

    String getDescription();

    void setDescription(String description);

    Icon getIcon();

    void setIcon(Icon icon);

    boolean isDisable();

    void setDisable(boolean disable);

    boolean isVisible();

    void setVisible(boolean visible);

    default boolean isSelected() {
        throw new UnsupportedOperationException("selected");
    }

    default void setSelected(boolean selected) {
        throw new UnsupportedOperationException("selected");
    }
}
