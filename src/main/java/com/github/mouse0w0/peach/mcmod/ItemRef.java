package com.github.mouse0w0.peach.mcmod;

import org.apache.commons.lang3.Validate;

import java.io.Serializable;

public class ItemRef implements Serializable {

    public static final int METADATA_WILDCARD = Short.MAX_VALUE;
    public static final int ORE_DICTIONARY = Short.MAX_VALUE + 1;

    public static ItemRef AIR = ItemRef.createItem("minecraft:air", 0);

    private final String id;
    private final int metadata;

    public static ItemRef createItem(String id, int metadata) {
        return new ItemRef(id, metadata);
    }

    public static ItemRef createIgnoreMetadata(String id) {
        return new ItemRef(id, METADATA_WILDCARD);
    }

    public static ItemRef createOreDict(String id) {
        return new ItemRef(id, ORE_DICTIONARY);
    }

    protected ItemRef(String id, int metadata) {
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
        ItemRef that = (ItemRef) o;
        return metadata == that.metadata && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode() * 31 + metadata;
    }
}
