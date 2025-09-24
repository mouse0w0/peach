package com.github.mouse0w0.peach.ui.layout;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class LayoutUtils {
    public static ScrollPane scrollVBox(Node... nodes) {
        ScrollPane scrollPane = new ScrollPane(new VBox(nodes));
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-vbox");
        return scrollPane;
    }

    public static TitledPane titled(String title, Node content) {
        TitledPane titledPane = new TitledPane(title, content);
        titledPane.setCollapsible(false);
        return titledPane;
    }
}
