package com.github.mouse0w0.peach.mcmod.model.mcj;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;

public enum McjAxis {
    X, Y, Z;

    public static class Persistence implements JsonSerializer<McjAxis>, JsonDeserializer<McjAxis> {

        @Override
        public JsonElement serialize(McjAxis src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public McjAxis deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return McjAxis.valueOf(json.getAsString().toUpperCase(Locale.ROOT));
        }
    }
}
