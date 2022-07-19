package com.github.mouse0w0.peach.javafx.geometry;

import javafx.geometry.Side;

public enum EightPos {
    TOP_LEFT(Side.TOP, Side.LEFT),
    TOP_RIGHT(Side.TOP, Side.RIGHT),
    BOTTOM_LEFT(Side.BOTTOM, Side.LEFT),
    BOTTOM_RIGHT(Side.BOTTOM, Side.RIGHT),
    LEFT_TOP(Side.LEFT, Side.TOP),
    LEFT_BOTTOM(Side.LEFT, Side.BOTTOM),
    RIGHT_TOP(Side.RIGHT, Side.TOP),
    RIGHT_BOTTOM(Side.RIGHT, Side.BOTTOM);

    private final Side primary;
    private final Side secondary;

    EightPos(Side primary, Side secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public Side getPrimary() {
        return primary;
    }

    public Side getSecondary() {
        return secondary;
    }
}
