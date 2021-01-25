package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.base.CaseFormat;

public enum UseAnimation implements Localizable {
    NONE,
    EAT,
    DRINK,
    BLOCK,
    BOW;

    private final String translationKey;

    UseAnimation() {
        this.translationKey = "useAnimation." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getLocalizedText() {
        return I18n.translate(translationKey);
    }
}
