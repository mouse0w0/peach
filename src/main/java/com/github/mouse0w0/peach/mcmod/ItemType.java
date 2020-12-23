package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.base.CaseFormat;

public enum ItemType {
    NORMAL,
    SWORD,
    TOOL,
    ARMOR,
    FOOD;

    private final String translationKey;

    ItemType() {
        this.translationKey = "itemType." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getLocalizedName() {
        return I18n.translate(translationKey);
    }
}
