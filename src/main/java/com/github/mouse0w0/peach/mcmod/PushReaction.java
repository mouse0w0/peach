package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.base.CaseFormat;

public enum PushReaction implements Localizable {
    /**
     * Inherit block material, see {@link Material}
     */
    INHERIT,
    NORMAL,
    DESTROY,
    BLOCK,
    IGNORE,
    PUSH_ONLY;

    private final String translationKey;

    PushReaction() {
        translationKey = "pushReaction." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    @Override
    public String getLocalizedText() {
        return I18n.translate(translationKey);
    }
}
