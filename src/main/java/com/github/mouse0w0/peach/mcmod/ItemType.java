package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.google.common.base.CaseFormat;

public enum ItemType implements Localizable {
    NORMAL,
    SWORD,
    TOOL,
    ARMOR,
    FOOD;

    public static final ItemType[] VALUES = values();

    private final String translationKey;

    ItemType() {
        this.translationKey = "itemType." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public String getLocalizedText() {
        return AppL10n.localize(translationKey);
    }
}
