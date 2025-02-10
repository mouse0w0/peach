package com.github.mouse0w0.peach.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LowerCaseEnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    private final Map<String, T> nameToConstants = new HashMap<>();
    private final Map<T, String> constantsToName = new HashMap<>();

    public LowerCaseEnumTypeAdapter(Class<T> enumClass) {
        for (T constant : enumClass.getEnumConstants()) {
            String convertedName = constant.name().toLowerCase(Locale.ROOT);
            nameToConstants.put(convertedName, constant);
            constantsToName.put(constant, convertedName);
        }
    }

    @Override
    public T read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return nameToConstants.get(in.nextString());
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(constantsToName.get(value));
        }
    }
}
