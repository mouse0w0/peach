package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.Label;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;

public class TooltipSkin implements Skin<Tooltip> {
    private Label label;
    private Tooltip tooltip;

    public TooltipSkin(Tooltip t) {
        this.tooltip = t;

        // Fix JavaFX missing snap size.
        label = new Label();
        label.contentDisplayProperty().bind(t.contentDisplayProperty());
        label.fontProperty().bind(t.fontProperty());
        label.graphicProperty().bind(t.graphicProperty());
        label.graphicTextGapProperty().bind(t.graphicTextGapProperty());
        label.textAlignmentProperty().bind(t.textAlignmentProperty());
        label.textOverrunProperty().bind(t.textOverrunProperty());
        label.textProperty().bind(t.textProperty());
        label.wrapTextProperty().bind(t.wrapTextProperty());
        label.minWidthProperty().bind(t.minWidthProperty());
        label.prefWidthProperty().bind(t.prefWidthProperty());
        label.maxWidthProperty().bind(t.maxWidthProperty());
        label.minHeightProperty().bind(t.minHeightProperty());
        label.prefHeightProperty().bind(t.prefHeightProperty());
        label.maxHeightProperty().bind(t.maxHeightProperty());

        // Fix JavaFX missing bind properties.
        Bindings.bindContent(label.getStyleClass(), t.getStyleClass());
        label.styleProperty().bind(t.styleProperty());
        label.idProperty().bind(t.idProperty());
    }

    @Override
    public Tooltip getSkinnable() {
        return tooltip;
    }

    @Override
    public Node getNode() {
        return label;
    }

    @Override
    public void dispose() {
        tooltip = null;
        label = null;
    }
}
