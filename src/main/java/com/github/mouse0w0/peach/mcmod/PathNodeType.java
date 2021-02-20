package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.base.CaseFormat;

public enum PathNodeType implements Localizable {
    INHERIT,
    BLOCKED,
    OPEN,
    WALKABLE,
    TRAPDOOR,
    FENCE,
    LAVA,
    WATER,
    RAIL,
    DANGER_FIRE,
    DAMAGE_FIRE,
    DANGER_CACTUS,
    DAMAGE_CACTUS,
    DANGER_OTHER,
    DAMAGE_OTHER,
    DOOR_OPEN,
    DOOR_WOOD_CLOSED,
    DOOR_IRON_CLOSED;

    private final String translationKey;

    PathNodeType() {
        translationKey = "pathNodeType." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    @Override
    public String getLocalizedText() {
        return I18n.translate(translationKey);
    }
}
