package com.github.mouse0w0.peach.mcmod.model.mcj;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;

public enum McjFacing {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static class Persistence implements JsonSerializer<McjFacing>, JsonDeserializer<McjFacing> {

        @Override
        public JsonElement serialize(McjFacing src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public McjFacing deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return McjFacing.valueOf(json.getAsString().toUpperCase(Locale.ROOT));
        }
    }
}
