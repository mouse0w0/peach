package com.github.mouse0w0.peach.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonUtils {
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

    public static Gson gson() {
        return GSON;
    }

    public static <T> T readJson(Path path, Class<T> classOfT) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            return JsonUtils.gson().fromJson(reader, classOfT);
        }
    }

    public static void writeJson(Path path, Object object) throws IOException {
        FileUtils.createFileIfNotExists(path);
        try (Writer writer = Files.newBufferedWriter(path)) {
            JsonUtils.gson().toJson(object, writer);
        }
    }
}
