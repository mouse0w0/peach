package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.google.common.base.CaseFormat;

public enum UseAnimation implements Localizable {
    NONE,
    EAT,
    DRINK,
    BLOCK,
    BOW;

    public static final UseAnimation[] VALUES = values();

    private final String translationKey;

    UseAnimation() {
        this.translationKey = "useAnimation." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getLocalizedText() {
        return AppL10n.localize(translationKey);
    }
}
