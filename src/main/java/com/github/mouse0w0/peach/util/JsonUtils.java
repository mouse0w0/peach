package com.github.mouse0w0.peach.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class JsonUtils {
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

    public static JsonArray jsonArray(Iterable<? extends JsonElement> iterable) {
        return jsonArray(iterable.iterator());
    }

    public static JsonArray jsonArray(Iterator<? extends JsonElement> iterator) {
        JsonArray array = new JsonArray();
        while (iterator.hasNext()) {
            array.add(iterator.next());
        }
        return array;
    }

    public static JsonArray jsonArray(JsonElement element) {
        JsonArray array = new JsonArray(1);
        array.add(element);
        return array;
    }

    public static JsonArray jsonArray(JsonElement... elements) {
        JsonArray array = new JsonArray(elements.length);
        for (int i = 0; i < elements.length; i++) {
            array.add(elements[i]);
        }
        return array;
    }

    public static JsonArray stringArray(Iterable<? extends String> iterable) {
        return stringArray(iterable.iterator());
    }

    public static JsonArray stringArray(Iterator<? extends String> iterator) {
        JsonArray array = new JsonArray();
        while (iterator.hasNext()) {
            array.add(iterator.next());
        }
        return array;
    }

    public static JsonArray stringArray(String string) {
        JsonArray array = new JsonArray(1);
        array.add(string);
        return array;
    }

    public static JsonArray stringArray(String... strings) {
        JsonArray array = new JsonArray(strings.length);
        for (int i = 0; i < strings.length; i++) {
            array.add(strings[i]);
        }
        return array;
    }

    public static JsonArray numberArray(Iterable<? extends Number> iterable) {
        return numberArray(iterable.iterator());
    }

    public static JsonArray numberArray(Iterator<? extends Number> iterator) {
        JsonArray array = new JsonArray();
        while (iterator.hasNext()) {
            array.add(iterator.next());
        }
        return array;
    }

    public static JsonArray numberArray(Number number) {
        JsonArray array = new JsonArray(1);
        array.add(number);
        return array;
    }

    public static JsonArray numberArray(Number... numbers) {
        JsonArray array = new JsonArray(numbers.length);
        for (int i = 0; i < numbers.length; i++) {
            array.add(numbers[i]);
        }
        return array;
    }

    public static JsonArray booleanArray(Iterable<? extends Boolean> iterable) {
        return booleanArray(iterable.iterator());
    }

    public static JsonArray booleanArray(Iterator<? extends Boolean> iterator) {
        JsonArray array = new JsonArray();
        while (iterator.hasNext()) {
            array.add(iterator.next());
        }
        return array;
    }

    public static JsonArray booleanArray(Boolean value) {
        JsonArray array = new JsonArray(1);
        array.add(value);
        return array;
    }

    public static JsonArray booleanArray(Boolean... values) {
        JsonArray array = new JsonArray(values.length);
        for (int i = 0; i < values.length; i++) {
            array.add(values[i]);
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
        return GSON.toJsonTree(src);
    }

    public static JsonElement toJson(Gson gson, Object src) {
        return gson.toJsonTree(src);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static <T> T fromJson(Gson gson, JsonElement json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(JsonElement json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(Gson gson, JsonElement json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(JsonElement json, TypeToken<T> typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(Gson gson, JsonElement json, TypeToken<T> typeOfT) {
        return gson.fromJson(json, typeOfT);
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

    public static <T> T readJson(Path file, TypeToken<T> typeOfT) throws IOException {
        return readJson(GSON, file, typeOfT);
    }

    public static <T> T readJson(Gson gson, Path file, TypeToken<T> typeOfT) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            return gson.fromJson(reader, typeOfT);
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

    public static <T> T readJson(URL url, TypeToken<T> typeOfT) throws IOException {
        return readJson(GSON, url, typeOfT);
    }

    public static <T> T readJson(Gson gson, URL url, TypeToken<T> typeOfT) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return gson.fromJson(reader, typeOfT);
        }
    }

    public static void writeJson(Path file, Object src) throws IOException {
        writeJson(GSON, file, src);
    }

    public static void writeJson(Gson gson, Path file, Object src) throws IOException {
        try (Writer writer = FileUtils.newBufferedWriter(file)) {
            gson.toJson(src, writer);
        }
    }
}
