package com.github.mouse0w0.peach.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public final class JsonUtils {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Gson GSON_PRETTY_PRINTING = new GsonBuilder().setPrettyPrinting().create();

    public static JsonPrimitive json(String value) {
        return new JsonPrimitive(value);
    }

    public static JsonPrimitive json(Number value) {
        return new JsonPrimitive(value);
    }

    public static JsonPrimitive json(Boolean value) {
        return new JsonPrimitive(value);
    }

    public static JsonArray jsonArray(Iterable<JsonElement> iterable) {
        return jsonArray(iterable.iterator());
    }

    public static JsonArray jsonArray(Iterator<JsonElement> iterator) {
        JsonArray array = new JsonArray();
        while (iterator.hasNext()) {
            array.add(iterator.next());
        }
        return array;
    }

    public static JsonArray jsonArray(JsonElement... elements) {
        JsonArray array = new JsonArray(elements.length);
        for (JsonElement element : elements) {
            array.add(element);
        }
        return array;
    }

    public static JsonArray jsonStringArray(Iterable<String> iterable) {
        return jsonStringArray(iterable.iterator());
    }

    public static JsonArray jsonStringArray(Iterator<String> iterator) {
        JsonArray array = new JsonArray();
        while (iterator.hasNext()) {
            array.add(iterator.next());
        }
        return array;
    }

    public static Gson gson() {
        return GSON;
    }

    public static Gson gsonPrettyPrinting() {
        return GSON_PRETTY_PRINTING;
    }

    public static JsonElement toJson(Object src) {
        return gson().toJsonTree(src);
    }

    public static JsonElement toJson(Gson gson, Object src) {
        return gson.toJsonTree(src);
    }

    public static <T> T fromJson(JsonElement jsonElement, Class<T> classOfT) {
        return gson().fromJson(jsonElement, classOfT);
    }

    public static <T> T fromJson(Gson gson, JsonElement jsonElement, Class<T> classOfT) {
        return gson.fromJson(jsonElement, classOfT);
    }

    public static <T> T fromJson(JsonElement jsonElement, Type typeOfT) {
        return gson().fromJson(jsonElement, typeOfT);
    }

    public static <T> T fromJson(Gson gson, JsonElement jsonElement, Type typeOfT) {
        return gson.fromJson(jsonElement, typeOfT);
    }

    public static <T> T fromJson(JsonElement jsonElement, TypeToken<T> typeToken) {
        return gson().fromJson(jsonElement, typeToken.getType());
    }

    public static <T> T fromJson(Gson gson, JsonElement jsonElement, TypeToken<T> typeToken) {
        return gson.fromJson(jsonElement, typeToken.getType());
    }

    public static JsonElement readJson(Path file) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return JsonParser.parseReader(reader);
        }
    }

    public static JsonElement readJson(URL url) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return JsonParser.parseReader(reader);
        }
    }

    public static <T> T readJson(Path file, Class<T> classOfT) throws IOException {
        return readJson(GSON, file, classOfT);
    }

    public static <T> T readJson(Gson gson, Path file, Class<T> classOfT) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return gson.fromJson(reader, classOfT);
        }
    }

    public static <T> T readJson(Path file, Type typeOfT) throws IOException {
        return readJson(GSON, file, typeOfT);
    }

    public static <T> T readJson(Gson gson, Path file, Type typeOfT) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return gson.fromJson(reader, typeOfT);
        }
    }

    public static <T> T readJson(Path file, TypeToken<T> typeToken) throws IOException {
        return readJson(GSON, file, typeToken);
    }

    public static <T> T readJson(Gson gson, Path file, TypeToken<T> typeToken) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return gson.fromJson(reader, typeToken.getType());
        }
    }

    public static <T> T readJson(URL url, Class<T> classOfT) throws IOException {
        return readJson(GSON, url, classOfT);
    }

    public static <T> T readJson(Gson gson, URL url, Class<T> classOfT) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return gson.fromJson(reader, classOfT);
        }
    }

    public static <T> T readJson(URL url, Type typeOfT) throws IOException {
        return readJson(GSON, url, typeOfT);
    }

    public static <T> T readJson(Gson gson, URL url, Type typeOfT) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return gson.fromJson(reader, typeOfT);
        }
    }

    public static <T> T readJson(URL url, TypeToken<T> typeToken) throws IOException {
        return readJson(GSON, url, typeToken);
    }

    public static <T> T readJson(Gson gson, URL url, TypeToken<T> typeToken) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return gson.fromJson(reader, typeToken.getType());
        }
    }

    public static void writeJson(Path file, Object object) throws IOException {
        writeJson(gson(), file, object);
    }

    public static void writeJson(Gson gson, Path file, Object object) throws IOException {
        FileUtils.createFileIfNotExists(file);
        try (Writer writer = Files.newBufferedWriter(file)) {
            gson.toJson(object, writer);
        }
    }

    private JsonUtils() {
    }
}
