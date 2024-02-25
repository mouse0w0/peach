package com.github.mouse0w0.peach.action;

import org.jetbrains.annotations.NotNull;

public final class Constraints {
    public static final Constraints FIRST = new Constraints(Anchor.FIRST, null);
    public static final Constraints LAST = new Constraints(Anchor.LAST, null);

    private final Anchor anchor;
    private final String relativeToActionId;

    public Constraints(@NotNull Anchor anchor, String relativeToActionId) {
        this.anchor = anchor;
        this.relativeToActionId = relativeToActionId;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public String getRelativeToActionId() {
        return relativeToActionId;
    }
}
