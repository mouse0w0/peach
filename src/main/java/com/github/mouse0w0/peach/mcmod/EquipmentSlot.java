package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.base.CaseFormat;

public enum EquipmentSlot {
    MAINHAND(Type.HAND),
    OFFHAND(Type.HAND),
    HEAD(Type.ARMOR),
    CHEST(Type.ARMOR),
    LEGS(Type.ARMOR),
    FEET(Type.ARMOR);

    public enum Type {
        HAND, ARMOR
    }

    private final Type type;
    private final String translationKey;

    EquipmentSlot(Type type) {
        this.type = type;
        this.translationKey = "equipmentSlot." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public Type getType() {
        return type;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getLocalizedName() {
        return I18n.translate(translationKey);
    }
}
