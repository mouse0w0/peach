package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.Tip;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class TipSkin implements Skin<Tip> {
    private Tip skinnable;

    private final HBox content;

    public TipSkin(Tip skinnable) {
        this.skinnable = skinnable;

        Label label = new Label();
        label.getStyleClass().setAll("label");
        label.textProperty().bind(skinnable.textProperty());
        label.graphicProperty().bind(skinnable.graphicProperty());

        Region close = new Region();
        close.getStyleClass().add("close");

        StackPane closeButton = new StackPane(close);
        closeButton.setAccessibleRole(AccessibleRole.BUTTON);
        closeButton.getStyleClass().setAll("close-button");
        closeButton.visibleProperty().bind(skinnable.closableProperty());
        closeButton.managedProperty().bind(skinnable.closableProperty());
        closeButton.setOnMousePressed(event -> getSkinnable().hide());

        content = new HBox();
        content.getStyleClass().setAll("content");
        content.getChildren().setAll(label, closeButton);
    }

    @Override
    public Tip getSkinnable() {
        return skinnable;
    }

    @Override
    public Node getNode() {
        return content;
    }

    @Override
    public void dispose() {
        skinnable = null;
    }
}
