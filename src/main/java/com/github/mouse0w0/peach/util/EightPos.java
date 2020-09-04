package com.github.mouse0w0.peach.util;

public enum EightPos {
    TOP_LEFT(FourPos.TOP, FourPos.LEFT),
    TOP_RIGHT(FourPos.TOP, FourPos.RIGHT),
    BOTTOM_LEFT(FourPos.BOTTOM, FourPos.LEFT),
    BOTTOM_RIGHT(FourPos.BOTTOM, FourPos.RIGHT),
    LEFT_TOP(FourPos.LEFT, FourPos.TOP),
    LEFT_BOTTOM(FourPos.LEFT, FourPos.BOTTOM),
    RIGHT_TOP(FourPos.RIGHT, FourPos.TOP),
    RIGHT_BOTTOM(FourPos.RIGHT, FourPos.BOTTOM);

    private final FourPos primary;
    private final FourPos secondary;

    EightPos(FourPos primary, FourPos secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public FourPos getPrimary() {
        return primary;
    }

    public FourPos getSecondary() {
        return secondary;
    }
}
