package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.base.CaseFormat;

public enum BlockType implements Localizable {
    NORMAL,
    PILLAR,
    HORIZONTAL,
    HORIZONTAL_OPPOSITE,
    SIX_DIRECTION,
    SIX_DIRECTION_OPPOSITE,
    GRASS,
    STAIR,
    SLAB,
    FENCE,
    FENCE_GATE,
    WALL,
    TRAP_DOOR,
    PANE;

    private final String translationKey;

    BlockType() {
        translationKey = "blockType." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public String getLocalizedText() {
        return I18n.translate(translationKey);
    }
}
