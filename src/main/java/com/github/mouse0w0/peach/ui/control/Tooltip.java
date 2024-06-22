package com.github.mouse0w0.peach.ui.control;

import com.github.mouse0w0.peach.ui.control.skin.TooltipSkin;
import javafx.scene.control.Skin;

public class Tooltip extends javafx.scene.control.Tooltip {
    public Tooltip() {
    }

    public Tooltip(String text) {
        super(text);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TooltipSkin(this);
    }
}
