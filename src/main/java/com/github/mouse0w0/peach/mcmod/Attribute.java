package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public final class Attribute {
    private static final Set<String> ATTRIBUTES;

    public static final String MAX_HEALTH = "generic.maxHealth";
    public static final String KNOCKBACK_RESISTANCE = "generic.knockbackResistance";
    public static final String MOVEMENT_SPEED = "generic.movementSpeed";
    public static final String FLYING_SPEED = "generic.flyingSpeed";
    public static final String ATTACK_DAMAGE = "generic.attackDamage";
    public static final String ATTACK_SPEED = "generic.attackSpeed";
    public static final String ARMOR = "generic.armor";
    public static final String ARMOR_TOUGHNESS = "generic.armorToughness";
    public static final String LUCK = "generic.luck";
    public static final String REACH_DISTANCE = "generic.reachDistance";
    public static final String SWIM_SPEED = "forge.swimSpeed";

    static {
        ATTRIBUTES = ImmutableSet.of(MAX_HEALTH, KNOCKBACK_RESISTANCE, MOVEMENT_SPEED,
                FLYING_SPEED, ATTACK_DAMAGE, ATTACK_SPEED, ARMOR, ARMOR_TOUGHNESS, LUCK,
                REACH_DISTANCE, SWIM_SPEED);
    }

    public static Set<String> getAttributes() {
        return ATTRIBUTES;
    }

    public static String getLocalizedName(String attribute) {
        return AppL10n.localize("attribute." + attribute);
    }

    private Attribute() {
    }
}
