package com.github.mouse0w0.peach.mcmod;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

public class ItemGroup implements LocalizableEx {
    private String id;
    private String translationKey;
    private ItemRef icon;

    private transient String localizedText;

    public ItemGroup(String id, String translationKey, ItemRef icon) {
        this.id = id;
        this.translationKey = translationKey;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public ItemRef getIcon() {
        return icon;
    }

    @Override
    public String getLocalizedText() {
        return localizedText;
    }

    @Override
    public void setLocalizedText(String text) {
        this.localizedText = text;
    }

    public static final class Persister extends TypeAdapter<ItemGroup> {

        private final Map<String, ItemGroup> itemGroupMap;

        public Persister(Map<String, ItemGroup> itemGroupMap) {
            this.itemGroupMap = itemGroupMap;
        }

        @Override
        public void write(JsonWriter out, ItemGroup value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.getId());
            }
        }

        @Override
        public ItemGroup read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else {
                return itemGroupMap.get(in.nextString());
            }
        }
    }
}
