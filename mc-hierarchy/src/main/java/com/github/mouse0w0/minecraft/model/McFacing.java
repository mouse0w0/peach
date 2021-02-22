package com.github.mouse0w0.minecraft.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Locale;

@JsonAdapter(McFacing.Persistence.class)
public enum McFacing {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static class Persistence extends TypeAdapter<McFacing> {
        @Override
        public void write(JsonWriter out, McFacing value) throws IOException {
            out.value(value.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public McFacing read(JsonReader in) throws IOException {
            return McFacing.valueOf(in.nextString().toUpperCase(Locale.ROOT));
        }
    }
}
