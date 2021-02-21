package com.github.mouse0w0.minecraft.model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;

public enum McFacing {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static class Persistence implements JsonSerializer<McFacing>, JsonDeserializer<McFacing> {

        @Override
        public JsonElement serialize(McFacing src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public McFacing deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return McFacing.valueOf(json.getAsString().toUpperCase(Locale.ROOT));
        }
    }
}
