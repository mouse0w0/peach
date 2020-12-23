package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.base.CaseFormat;

public enum EnchantmentType {
    ALL,
    ARMOR,
    ARMOR_FEET,
    ARMOR_LEGS,
    ARMOR_CHEST,
    ARMOR_HEAD,
    WEAPON,
    DIGGER,
    FISHING_ROD,
    BREAKABLE,
    BOW,
    WEARABLE;

    public static final EnchantmentType[] EMPTY_ARRAY = new EnchantmentType[0];

    private final String translationKey;

    EnchantmentType() {
        this.translationKey = "enchantmentType." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getLocalizedName() {
        return I18n.translate(translationKey);
    }
}
