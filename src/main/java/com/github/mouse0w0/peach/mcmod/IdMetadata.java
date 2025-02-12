package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.util.Validate;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;

@JsonAdapter(IdMetadata.TypeAdapter.class)
public final class IdMetadata implements Serializable {
    public static final int METADATA_WILDCARD = Short.MAX_VALUE;
    public static final int ORE_DICTIONARY = Short.MAX_VALUE + 1;

    public static final String ORE_NAMESPACE = "ore";

    private static final IdMetadata AIR = IdMetadata.of("minecraft:air");

    private final Identifier identifier;
    private final int metadata;

    public static IdMetadata air() {
        return AIR;
    }

    public static IdMetadata of(String str) {
        int separatorIndex = str.indexOf('#');
        if (separatorIndex == -1) {
            return new IdMetadata(Identifier.of(str), 0);
        } else {
            return new IdMetadata(Identifier.of(str.substring(0, separatorIndex)), Integer.parseInt(str.substring(separatorIndex + 1)));
        }
    }

    public static IdMetadata of(Identifier identifier) {
        return new IdMetadata(identifier, 0);
    }

    public static IdMetadata of(String identifier, int metadata) {
        return new IdMetadata(Identifier.of(identifier), metadata);
    }

    public static IdMetadata of(Identifier identifier, int metadata) {
        return new IdMetadata(identifier, metadata);
    }

    public static IdMetadata ofIgnoreMetadata(String identifier) {
        return new IdMetadata(Identifier.of(identifier), METADATA_WILDCARD);
    }

    public static IdMetadata ofIgnoreMetadata(Identifier identifier) {
        return new IdMetadata(identifier, METADATA_WILDCARD);
    }

    public static IdMetadata ofOreDictionary(String ore) {
        return new IdMetadata(Identifier.of(ORE_NAMESPACE, ore), ORE_DICTIONARY);
    }

    private IdMetadata(Identifier identifier, int metadata) {
        this.identifier = Validate.notNull(identifier);
        this.metadata = metadata;
    }

    public Identifier getIdentifier() {
        return identifier;
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
        if (o == null || getClass() != o.getClass()) return false;

        IdMetadata that = (IdMetadata) o;
        return metadata == that.metadata && identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode() * 31 + metadata;
    }

    @Override
    public String toString() {
        return metadata == 0 ? identifier.toString() : identifier.toString() + "#" + metadata;
    }

    public static final class TypeAdapter extends com.google.gson.TypeAdapter<IdMetadata> {
        @Override
        public void write(JsonWriter out, IdMetadata value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public IdMetadata read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                return IdMetadata.of(in.nextString());
            }
        }
    }
}
