package com.github.mouse0w0.peach.ui.control;

import com.github.mouse0w0.peach.ui.control.skin.LabelSkin;
import javafx.scene.Node;
import javafx.scene.control.Skin;

public class Label extends javafx.scene.control.Label {
    public Label() {
    }

    public Label(String text) {
        super(text);
    }

    public Label(String text, Node graphic) {
        super(text, graphic);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabelSkin(this);
    }
}
