package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.google.common.base.CaseFormat;

public enum EquipmentSlot implements Localizable {
    NONE(Type.NONE),
    MAINHAND(Type.HAND),
    OFFHAND(Type.HAND),
    HEAD(Type.ARMOR),
    CHEST(Type.ARMOR),
    LEGS(Type.ARMOR),
    FEET(Type.ARMOR);

    public enum Type {
        NONE, HAND, ARMOR
    }

    public static final EquipmentSlot[] HAND_SLOTS = {NONE, MAINHAND, OFFHAND};
    public static final EquipmentSlot[] ARMOR_SLOTS = {NONE, HEAD, CHEST, LEGS, FEET};

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
        return AppL10n.localize(translationKey);
    }
}
