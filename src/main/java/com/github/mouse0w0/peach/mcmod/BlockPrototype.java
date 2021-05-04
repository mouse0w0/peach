package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.base.CaseFormat;

public enum BlockPrototype implements Localizable {
    NONE,
    PILLAR,
    HORIZONTAL_FOR_PLAYER,
    HORIZONTAL_FOR_BLOCK,
    //    VERTICAL,
    SIX_DIRECTION_FOR_PLAYER,
    SIX_DIRECTION_FOR_BLOCK,
    STAIR,
    SLAB,
    FENCE,
    FENCE_GATE,
    WALL,
    TRAP_DOOR,
    PANE;

    private final String translationKey;

    BlockPrototype() {
        translationKey = "blockPrototype." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public String getLocalizedText() {
        return I18n.translate(translationKey);
    }
}
