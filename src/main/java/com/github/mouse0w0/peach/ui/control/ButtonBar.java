package com.github.mouse0w0.peach.ui.control;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class ButtonBar extends HBox {
    public ButtonBar() {
        getStyleClass().add("button-bar");
    }

    public ButtonBar(double spacing) {
        this();
        setSpacing(spacing);
    }

    public ButtonBar(Node... children) {
        this();
        getChildren().addAll(children);
    }

    public ButtonBar(double spacing, Node... children) {
        this();
        setSpacing(spacing);
        getChildren().addAll(children);
    }

    @Override
    public String getUserAgentStylesheet() {
        return ButtonBar.class.getResource("ButtonBar.css").toExternalForm();
    }
}
