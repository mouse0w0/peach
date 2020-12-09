package com.github.mouse0w0.peach.form;

public enum ColSpan {
    ONE(12),
    FIVE_SIXTH(10),
    TWO_THIRD(8),
    HALF(6),
    THIRD(4),
    QUARTER(3),
    SIXTH(2),
    TWELFTH(1);

    private final int span;

    ColSpan(int span) {
        this.span = span;
    }

    public int getSpan() {
        return span;
    }
}
