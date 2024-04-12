package com.github.mouse0w0.peach.mcmod;

import java.io.Serializable;

public final class IdMetadata implements Serializable {
    public static IdMetadata AIR = IdMetadata.of("minecraft:air");

    public static final int METADATA_WILDCARD = Short.MAX_VALUE;
    public static final int ORE_DICTIONARY = Short.MAX_VALUE + 1;

    private final Identifier id;
    private final int metadata;

    public static IdMetadata of(String id) {
        return new IdMetadata(Identifier.of(id), 0);
    }

    public static IdMetadata of(Identifier id) {
        return new IdMetadata(id, 0);
    }

    public static IdMetadata of(String id, int metadata) {
        return new IdMetadata(Identifier.of(id), metadata);
    }

    public static IdMetadata of(Identifier id, int metadata) {
        return new IdMetadata(id, metadata);
    }

    public static IdMetadata ignoreMetadata(String id) {
        return new IdMetadata(Identifier.of(id), METADATA_WILDCARD);
    }


    public static IdMetadata ignoreMetadata(Identifier id) {
        return new IdMetadata(id, METADATA_WILDCARD);
    }

    public static IdMetadata oreDictionary(String id) {
        return new IdMetadata(Identifier.of("ore", id), ORE_DICTIONARY);
    }

    private IdMetadata(Identifier id, int metadata) {
        this.id = id;
        this.metadata = metadata;
    }

    public Identifier getId() {
        return id;
    }

    public int getMetadata() {
        return metadata;
    }

    public boolean isAir() {
        return AIR.equals(this);
    }

    public boolean isNormal() {
        return metadata < METADATA_WILDCARD;
    }

    public boolean isIgnoreMetadata() {
        return metadata == METADATA_WILDCARD;
    }

    public boolean isOreDictionary() {
        return metadata == ORE_DICTIONARY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdMetadata that = (IdMetadata) o;
        return metadata == that.metadata && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode() * 31 + metadata;
    }
}
