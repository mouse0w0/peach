package com.github.mouse0w0.peach.javafx.control.skin;

import com.github.mouse0w0.peach.javafx.control.PopupAlert;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class PopupAlertSkin implements Skin<PopupAlert> {

    private PopupAlert skinnable;

    private final HBox content;

    public PopupAlertSkin(PopupAlert skinnable) {
        this.skinnable = skinnable;

        Label label = new Label();
        label.getStyleClass().setAll("label");
        label.textProperty().bind(skinnable.textProperty());
        label.graphicProperty().bind(skinnable.graphicProperty());

        StackPane closeButton = new StackPane();
        closeButton.setAccessibleRole(AccessibleRole.BUTTON);
        closeButton.getStyleClass().setAll("close-button");
        closeButton.visibleProperty().bind(skinnable.closableProperty());
        closeButton.managedProperty().bind(skinnable.closableProperty());
        closeButton.setOnMousePressed(event -> getSkinnable().hide());

        content = new HBox();
        content.getStyleClass().setAll("content");
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().setAll(label, closeButton);
    }

    @Override
    public PopupAlert getSkinnable() {
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
