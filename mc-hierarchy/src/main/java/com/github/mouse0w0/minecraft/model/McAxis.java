package com.github.mouse0w0.minecraft.model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;

public enum McAxis {
    X, Y, Z;

    public static class Persistence implements JsonSerializer<McAxis>, JsonDeserializer<McAxis> {

        @Override
        public JsonElement serialize(McAxis src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public McAxis deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return McAxis.valueOf(json.getAsString().toUpperCase(Locale.ROOT));
        }
    }
}
