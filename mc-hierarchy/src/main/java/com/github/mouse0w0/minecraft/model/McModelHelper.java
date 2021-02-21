package com.github.mouse0w0.minecraft.model;

import com.google.gson.*;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class McModelHelper {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(McModel.class, new McModel.Serializer())
            .registerTypeAdapter(McTransform.class, new McTransform.Serializer())
            .registerTypeAdapter(McElement.class, new McElement.Serializer())
            .registerTypeAdapter(McElement.Rotation.class, new McElement.Rotation.Serializer())
            .registerTypeAdapter(McAxis.class, new McAxis.Persistence())
            .registerTypeAdapter(McFacing.class, new McFacing.Persistence())
            .registerTypeAdapter(McFace.class, new McFace.Serializer())
            .registerTypeAdapter(Vector3f.class, new Vector3fPersistence())
            .registerTypeAdapter(Vector4f.class, new Vector4fPersistence())
            .create();

    public static McModel load(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return load(reader);
        }
    }

    public static McModel load(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return load(reader);
        }
    }

    public static McModel load(Reader reader) {
        return GSON.fromJson(reader, McModel.class);
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
