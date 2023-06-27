package com.github.mouse0w0.peach.file;

import com.github.mouse0w0.peach.icon.Icon;
import javafx.scene.Node;

public interface FileCell {
    String getText();

    void setText(String text);

    Node getGraphic();

    void setGraphic(Node graphic);

    Icon getIcon();

    void setIcon(Icon icon);
}
