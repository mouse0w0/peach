package com.github.mouse0w0.peach.action;

import org.jetbrains.annotations.NotNull;

public final class Order {
    public static final Order FIRST = new Order(Anchor.FIRST, null);
    public static final Order LAST = new Order(Anchor.LAST, null);

    private final Anchor anchor;
    private final String relativeToActionId;

    public Order(@NotNull Anchor anchor, String relativeToActionId) {
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
