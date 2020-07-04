package com.github.mouse0w0.peach.forge;

import org.apache.commons.lang3.Validate;

public class ItemToken {

    public static final int METADATA_WILDCARD = Short.MAX_VALUE;

    private String id;
    private int metadata;
    private boolean isOreDict;

    public static ItemToken createItemToken(String id, int metadata) {
        return new ItemToken(id, metadata, false);
    }

    public static ItemToken createIgnoreMetadataToken(String id) {
        return new ItemToken(id, METADATA_WILDCARD, false);
    }

    public static ItemToken createOreDictToken(String id) {
        return new ItemToken(id, 0, true);
    }

    protected ItemToken(String id, int metadata, boolean isOreDict) {
        this.id = Validate.notEmpty(id);
        this.metadata = metadata;
        this.isOreDict = isOreDict;
    }

    public String getId() {
        return id;
    }

    public int getMetadata() {
        return metadata;
    }

    public boolean isIgnoreMetadata() {
        return metadata == METADATA_WILDCARD;
    }

    public boolean isOreDict() {
        return isOreDict;
    }

    public boolean isStandard() {
        return !isIgnoreMetadata() && !isOreDict();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemToken that = (ItemToken) o;
        return metadata == that.metadata && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode() * 31 + metadata;
    }
}
