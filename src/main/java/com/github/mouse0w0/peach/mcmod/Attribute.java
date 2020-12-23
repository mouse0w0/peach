package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class Attribute {
    private static final Map<String, Attribute> ATTRIBUTES = new LinkedHashMap<>();

    public static final Attribute MAX_HEALTH = of("generic.maxHealth");
    public static final Attribute KNOCKBACK_RESISTANCE = of("generic.knockbackResistance");
    public static final Attribute MOVEMENT_SPEED = of("generic.movementSpeed");
    public static final Attribute FLYING_SPEED = of("generic.flyingSpeed");
    public static final Attribute ATTACK_DAMAGE = of("generic.attackDamage");
    public static final Attribute ATTACK_SPEED = of("generic.attackSpeed");
    public static final Attribute ARMOR = of("generic.armor");
    public static final Attribute ARMOR_TOUGHNESS = of("generic.armorToughness");
    public static final Attribute LUCK = of("generic.luck");
    public static final Attribute REACH_DISTANCE = of("generic.reachDistance");
    public static final Attribute SWIM_SPEED = of("forge.swimSpeed");

    public static Map<String, Attribute> getAttributes() {
        return ATTRIBUTES;
    }

    public static Attribute of(String name) {
        return ATTRIBUTES.computeIfAbsent(name, Attribute::new);
    }

    private final String name;

    private Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLocalizedName() {
        return I18n.translate("attribute." + name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return name.equals(attribute.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                '}';
    }
}
