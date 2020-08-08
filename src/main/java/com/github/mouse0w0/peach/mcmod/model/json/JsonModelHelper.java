package com.github.mouse0w0.peach.mcmod.model.json;

import com.google.gson.*;
import org.joml.Vector3d;
import org.joml.Vector3i;
import org.joml.Vector4i;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonModelHelper {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Vector3d.class, new Vector3dPersistence())
            .registerTypeAdapter(Vector3i.class, new Vector3iPersistence())
            .registerTypeAdapter(Vector4i.class, new Vector4iPersistence())
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

    private static class Vector3dPersistence implements JsonSerializer<Vector3d>, JsonDeserializer<Vector3d> {

        @Override
        public Vector3d deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray array = json.getAsJsonArray();
            return new Vector3d(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
        }

        @Override
        public JsonElement serialize(Vector3d src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray array = new JsonArray();
            array.add(src.x);
            array.add(src.y);
            array.add(src.z);
            return array;
        }
    }

    private static class Vector3iPersistence implements JsonSerializer<Vector3i>, JsonDeserializer<Vector3i> {

        @Override
        public Vector3i deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray array = json.getAsJsonArray();
            return new Vector3i(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
        }

        @Override
        public JsonElement serialize(Vector3i src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray array = new JsonArray();
            array.add(src.x);
            array.add(src.y);
            array.add(src.z);
            return array;
        }
    }

    private static class Vector4iPersistence implements JsonSerializer<Vector4i>, JsonDeserializer<Vector4i> {

        @Override
        public Vector4i deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray array = json.getAsJsonArray();
            return new Vector4i(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt(), array.get(3).getAsInt());
        }

        @Override
        public JsonElement serialize(Vector4i src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray array = new JsonArray();
            array.add(src.x);
            array.add(src.y);
            array.add(src.z);
            array.add(src.w);
            return array;
        }
    }
}
