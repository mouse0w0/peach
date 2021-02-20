package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.base.CaseFormat;

public enum RenderType implements Localizable {
    SOLID,
    CUTOUT_MIPPED,
    CUTOUT,
    TRANSLUCENT;

    private final String translationKey;

    RenderType() {
        translationKey = "renderType." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    @Override
    public String getLocalizedText() {
        return I18n.translate(translationKey);
    }
}
