package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Axis;
import com.github.mouse0w0.peach.mcmod.Direction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class ModelLoader {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Vector3f.class, new Vector3fTypeAdapter())
            .registerTypeAdapter(Vector4f.class, new Vector4fTypeAdapter())
            .registerTypeAdapter(Direction.class, new DirectionTypeAdapter())
            .registerTypeAdapter(Axis.class, new AxisTypeAdapter())
            .create();

    public static Model load(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return load(reader);
        }
    }

    public static Model load(URL url) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
            return load(reader);
        }
    }

    public static Model load(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return load(reader);
        }
    }

    public static Model load(Reader reader) {
        return GSON.fromJson(reader, Model.class);
    }

    public static class Vector3fTypeAdapter extends TypeAdapter<Vector3f> {
        @Override
        public void write(JsonWriter out, Vector3f value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.beginArray();
                out.value(normalizeFloat(value.x));
                out.value(normalizeFloat(value.y));
                out.value(normalizeFloat(value.z));
                out.endArray();
            }
        }

        @Override
        public Vector3f read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else {
                in.beginArray();
                Vector3f v = new Vector3f((float) in.nextDouble(), (float) in.nextDouble(), (float) in.nextDouble());
                in.endArray();
                return v;
            }
        }
    }

    public static class Vector4fTypeAdapter extends TypeAdapter<Vector4f> {
        @Override
        public void write(JsonWriter out, Vector4f value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.beginArray();
                out.value(normalizeFloat(value.x));
                out.value(normalizeFloat(value.y));
                out.value(normalizeFloat(value.z));
                out.value(normalizeFloat(value.w));
                out.endArray();
            }
        }

        @Override
        public Vector4f read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else {
                in.beginArray();
                Vector4f v = new Vector4f(
                        (float) in.nextDouble(), (float) in.nextDouble(),
                        (float) in.nextDouble(), (float) in.nextDouble());
                in.endArray();
                return v;
            }
        }
    }

    public static class DirectionTypeAdapter extends TypeAdapter<Direction> {
        @Override
        public void write(JsonWriter out, Direction value) throws IOException {
            out.value(value.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public Direction read(JsonReader in) throws IOException {
            return Direction.valueOf(in.nextString().toUpperCase(Locale.ROOT));
        }
    }

    public static class AxisTypeAdapter extends TypeAdapter<Axis> {
        @Override
        public void write(JsonWriter out, Axis value) throws IOException {
            out.value(value.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public Axis read(JsonReader in) throws IOException {
            return Axis.valueOf(in.nextString().toUpperCase(Locale.ROOT));
        }
    }

    private static Number normalizeFloat(float value) {
        int i = (int) value;
        if (i == value) return i;
        else return value;
    }
}
