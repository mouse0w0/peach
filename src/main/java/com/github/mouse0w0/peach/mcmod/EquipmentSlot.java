package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.base.CaseFormat;

public enum EquipmentSlot implements Localizable {
    MAINHAND(Type.HAND),
    OFFHAND(Type.HAND),
    HEAD(Type.ARMOR),
    CHEST(Type.ARMOR),
    LEGS(Type.ARMOR),
    FEET(Type.ARMOR);

    public enum Type {
        HAND, ARMOR
    }

    public static final EquipmentSlot[] HAND_SLOTS = {MAINHAND, OFFHAND};
    public static final EquipmentSlot[] ARMOR_SLOTS = {HEAD, CHEST, LEGS, FEET};

    private final Type type;
    private final String translationKey;

    EquipmentSlot(Type type) {
        this.type = type;
        this.translationKey = "equipmentSlot." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public Type getType() {
        return type;
    }

    public String getLocalizedText() {
        return I18n.translate(translationKey);
    }
}
