package com.github.mouse0w0.peach.ui.control.skin;

import javafx.scene.control.Label;

public class LabelSkin extends javafx.scene.control.skin.LabelSkin {
    public LabelSkin(Label control) {
        super(control);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        // Fix JavaFX missing snap size.
        return snapSizeX(super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset));
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        // Fix JavaFX missing snap size.
        return snapSizeY(super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset));
    }
}
