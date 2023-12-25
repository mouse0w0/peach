package com.github.mouse0w0.peach.mcmod;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

public class Material implements LocalizableWithItemIcon {
    private String id;
    private String translationKey;
    private IdMetadata icon;

    private transient String localizedText;

    public Material(String id, String translationKey, IdMetadata icon) {
        this.id = id;
        this.translationKey = translationKey;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public IdMetadata getIcon() {
        return icon;
    }

    @Override
    public String getLocalizedText() {
        return localizedText;
    }

    public void setLocalizedText(String text) {
        this.localizedText = text;
    }

    public static final class Persister extends TypeAdapter<Material> {

        private final Map<String, Material> materialMap;

        public Persister(Map<String, Material> materialMap) {
            this.materialMap = materialMap;
        }

        @Override
        public void write(JsonWriter out, Material value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.getId());
            }
        }

        @Override
        public Material read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else {
                return materialMap.get(in.nextString());
            }
        }
    }
}
