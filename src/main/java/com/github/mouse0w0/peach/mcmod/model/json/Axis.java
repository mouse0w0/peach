package com.github.mouse0w0.peach.mcmod.model.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Locale;

public enum Axis {
    X, Y, Z;

    public static class Persistence implements JsonSerializer<Axis>, JsonDeserializer<Axis> {

        @Override
        public JsonElement serialize(Axis src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public Axis deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Axis.valueOf(json.getAsString().toUpperCase(Locale.ROOT));
        }
    }
}
