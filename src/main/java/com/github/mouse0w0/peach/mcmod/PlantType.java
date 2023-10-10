package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.google.common.base.CaseFormat;

public enum PlantType implements Localizable {
    NONE,
    PLAINS,
    DESERT,
    BEACH,
    CAVE,
    WATER,
    NETHER,
    CROP;

    public static final PlantType[] VALUES = values();

    private final String translationKey;

    PlantType() {
        translationKey = "plantType." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    @Override
    public String getLocalizedText() {
        return AppL10n.localize(translationKey);
    }
}
