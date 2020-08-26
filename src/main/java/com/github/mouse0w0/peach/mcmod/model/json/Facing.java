package com.github.mouse0w0.peach.mcmod.model.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;

public enum Facing {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static class Persistence implements JsonSerializer<Facing>, JsonDeserializer<Facing> {

        @Override
        public JsonElement serialize(Facing src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public Facing deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Facing.valueOf(json.getAsString().toUpperCase(Locale.ROOT));
        }
    }
}
