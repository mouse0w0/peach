package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.model;

import com.github.mouse0w0.coffeemaker.template.Field;

public class ItemGroupDef {
    private final String name;
    private final Field field;

    public ItemGroupDef(String name, Field field) {
        this.name = name;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public Field getField() {
        return field;
    }
}
