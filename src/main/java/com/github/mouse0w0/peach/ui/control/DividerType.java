package com.github.mouse0w0.peach.ui.control;

import javafx.geometry.Orientation;
import javafx.geometry.Side;

public enum DividerType {
    TOP_PRIMARY(Side.TOP, true),
    TOP_SECONDARY(Side.TOP, false),
    LEFT_PRIMARY(Side.LEFT, true),
    LEFT_SECONDARY(Side.LEFT, false),
    BOTTOM_PRIMARY(Side.BOTTOM, true),
    BOTTOM_SECONDARY(Side.BOTTOM, false),
    RIGHT_PRIMARY(Side.RIGHT, true),
    RIGHT_SECONDARY(Side.RIGHT, false);

    private final Side side;
    private final boolean primary;
    private final Orientation orientation;

    DividerType(Side side, boolean primary) {
        this.side = side;
        this.primary = primary;

        if (side.isHorizontal()) {
            orientation = primary ? Orientation.HORIZONTAL : Orientation.VERTICAL;
        } else {
            orientation = primary ? Orientation.VERTICAL : Orientation.HORIZONTAL;
        }
    }

    public Side getSide() {
        return side;
    }

    public boolean isPrimary() {
        return primary;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isVertical() {
        return orientation == Orientation.VERTICAL;
    }

    public boolean isHorizontal() {
        return orientation == Orientation.HORIZONTAL;
    }
}
