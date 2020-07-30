package com.github.mouse0w0.peach.mcmod;

import org.apache.commons.lang3.Validate;

public class Item {

    public static final int METADATA_WILDCARD = Short.MAX_VALUE;
    public static final int ORE_DICTIONARY = Short.MAX_VALUE + 1;

    public static Item AIR = Item.createItem("minecraft:air", 0);

    private String id;
    private int metadata;

    public static Item createItem(String id, int metadata) {
        return new Item(id, metadata);
    }

    public static Item createIgnoreMetadata(String id) {
        return new Item(id, METADATA_WILDCARD);
    }

    public static Item createOreDict(String id) {
        return new Item(id, ORE_DICTIONARY);
    }

    protected Item(String id, int metadata) {
        this.id = Validate.notEmpty(id);
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public int getMetadata() {
        return metadata;
    }

    public boolean isAir() {
        return this.equals(AIR);
    }

    public boolean isNormal() {
        return metadata < METADATA_WILDCARD;
    }

    public boolean isIgnoreMetadata() {
        return metadata == METADATA_WILDCARD;
    }

    public boolean isOreDict() {
        return metadata == ORE_DICTIONARY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item that = (Item) o;
        return metadata == that.metadata && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode() * 31 + metadata;
    }
}
