package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.Tip;
import javafx.beans.binding.Bindings;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class TipSkin implements Skin<Tip> {
    private Tip skinnable;
    private HBox root;

    public TipSkin(Tip tip) {
        this.skinnable = tip;

        Label label = new Label();
        label.textProperty().bind(tip.textProperty());
        label.graphicProperty().bind(tip.graphicProperty());

        StackPane closeButton = new StackPane();
        closeButton.setAccessibleRole(AccessibleRole.BUTTON);
        closeButton.getStyleClass().add("close-button");
        closeButton.visibleProperty().bind(tip.closableProperty());
        closeButton.managedProperty().bind(tip.closableProperty());
        closeButton.setOnMousePressed(event -> getSkinnable().hide());

        root = new HBox(label, closeButton);
        Bindings.bindContent(root.getStyleClass(), tip.getStyleClass());
        root.idProperty().bind(tip.idProperty());
        root.styleProperty().bind(tip.styleProperty());
        root.getStylesheets().add(Tip.class.getResource("Tip.css").toExternalForm());
    }

    @Override
    public Tip getSkinnable() {
        return skinnable;
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public void dispose() {
        skinnable = null;
        root = null;
    }
}
