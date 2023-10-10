package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.google.common.base.CaseFormat;

public enum OffsetType implements Localizable {
    NONE,
    XZ,
    XYZ;

    public static final OffsetType[] VALUES = values();

    private final String translationKey;

    OffsetType() {
        translationKey = "offsetType." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    @Override
    public String getLocalizedText() {
        return AppL10n.localize(translationKey);
    }
}
