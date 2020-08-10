package com.github.mouse0w0.peach.mcmod.model.json;

import com.google.gson.*;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonModelHelper {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(JsonModel.class, new JsonModel.Serializer())
            .registerTypeAdapter(Transform.class, new Transform.Serializer())
            .registerTypeAdapter(Element.class, new Element.Serializer())
            .registerTypeAdapter(Element.Rotation.class, new Element.Rotation.Serializer())
            .registerTypeAdapter(Face.class, new Face.Serializer())
            .registerTypeAdapter(Vector3f.class, new Vector3fPersistence())
            .registerTypeAdapter(Vector4f.class, new Vector4fPersistence())
            .setPrettyPrinting()
            .create();

    public static JsonModel load(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return load(reader);
        }
    }

    public static JsonModel load(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return load(reader);
        }
    }

    public static JsonModel load(Reader reader) {
        return GSON.fromJson(reader, JsonModel.class);
    }

    private static class Vector3fPersistence implements JsonSerializer<Vector3f>, JsonDeserializer<Vector3f> {

        @Override
        public Vector3f deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray array = json.getAsJsonArray();
            return new Vector3f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat());
        }

        @Override
        public JsonElement serialize(Vector3f src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray array = new JsonArray();
            array.add(normalizeFloat(src.x));
            array.add(normalizeFloat(src.y));
            array.add(normalizeFloat(src.z));
            return array;
        }
    }

    private static class Vector4fPersistence implements JsonSerializer<Vector4f>, JsonDeserializer<Vector4f> {

        @Override
        public Vector4f deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray array = json.getAsJsonArray();
            return new Vector4f(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat(), array.get(3).getAsFloat());
        }

        @Override
        public JsonElement serialize(Vector4f src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray array = new JsonArray();
            array.add(normalizeFloat(src.x));
            array.add(normalizeFloat(src.y));
            array.add(normalizeFloat(src.z));
            array.add(normalizeFloat(src.w));
            return array;
        }
    }

    private static Number normalizeFloat(float value) {
        int i = (int) value;
        if (i == value) return i;
        else return value;
    }
}
