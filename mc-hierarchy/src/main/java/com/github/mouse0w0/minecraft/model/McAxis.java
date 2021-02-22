package com.github.mouse0w0.minecraft.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Locale;

@JsonAdapter(McAxis.Persistence.class)
public enum McAxis {
    X, Y, Z;

    public static class Persistence extends TypeAdapter<McAxis> {
        @Override
        public void write(JsonWriter out, McAxis value) throws IOException {
            out.value(value.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public McAxis read(JsonReader in) throws IOException {
            return McAxis.valueOf(in.nextString().toUpperCase(Locale.ROOT));
        }
    }
}
