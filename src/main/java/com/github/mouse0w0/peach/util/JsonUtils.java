package com.github.mouse0w0.peach.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JsonUtils {
    private static final Gson GSON = new GsonBuilder().create();

    public static JsonPrimitive json(String value) {
        return new JsonPrimitive(value);
    }

    public static JsonPrimitive json(Number value) {
        return new JsonPrimitive(value);
    }

    public static JsonPrimitive json(Boolean value) {
        return new JsonPrimitive(value);
    }

    public static JsonArray json(Iterable<String> elements) {
        JsonArray array = new JsonArray();
        for (String element : elements) {
            array.add(element);
        }
        return array;
    }

    public static JsonArray jsonArray(Iterable<JsonElement> elements) {
        JsonArray array = new JsonArray();
        for (JsonElement element : elements) {
            array.add(element);
        }
        return array;
    }

    public static JsonArray jsonArray(JsonElement... elements) {
        JsonArray array = new JsonArray();
        for (JsonElement element : elements) {
            array.add(element);
        }
        return array;
    }

    public static Gson gson() {
        return GSON;
    }

    public static JsonElement toJson(Object src) {
        return GSON.toJsonTree(src);
    }

    public static <T> T fromJson(JsonElement jsonElement, Class<T> classOfT) {
        return gson().fromJson(jsonElement, classOfT);
    }

    public static <T> T fromJson(JsonElement jsonElement, Type typeOfT) {
        return gson().fromJson(jsonElement, typeOfT);
    }

    public static <T> T fromJson(JsonElement jsonElement, TypeToken<T> typeToken) {
        return gson().fromJson(jsonElement, typeToken.getType());
    }

    public static <T> T readJson(Path file, Class<T> classOfT) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return gson().fromJson(reader, classOfT);
        }
    }

    public static <T> T readJson(Path file, Type typeOfT) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return gson().fromJson(reader, typeOfT);
        }
    }

    public static <T> T readJson(Path file, TypeToken<T> typeToken) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return gson().fromJson(reader, typeToken.getType());
        }
    }

    public static void writeJson(Path file, Object object) throws IOException {
        FileUtils.createFileIfNotExists(file);
        try (Writer writer = Files.newBufferedWriter(file)) {
            gson().toJson(object, writer);
        }
    }

    private JsonUtils() {
    }
}
