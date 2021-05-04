package com.github.mouse0w0.peach.file;

import javafx.scene.Node;
import javafx.scene.image.Image;

public interface FileCell {
    String getText();

    void setText(String text);

    Node getGraphic();

    void setGraphic(Node graphic);

    Image getIcon();

    void setIcon(Image icon);
}
