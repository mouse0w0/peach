package com.github.mouse0w0.peach.mcmod;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

public class SoundType implements LocalizableWithItemIcon {
    private String id;
    private String translationKey;
    private ItemRef icon;

    private transient String localizedText;

    public SoundType(String id, String translationKey, ItemRef icon) {
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
    public ItemRef getIcon() {
        return icon;
    }

    @Override
    public String getLocalizedText() {
        return localizedText;
    }

    public void setLocalizedText(String text) {
        this.localizedText = text;
    }

    public static final class Persister extends TypeAdapter<SoundType> {

        private final Map<String, SoundType> soundTypeMap;

        public Persister(Map<String, SoundType> soundTypeMap) {
            this.soundTypeMap = soundTypeMap;
        }

        @Override
        public void write(JsonWriter out, SoundType value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.getId());
            }
        }

        @Override
        public SoundType read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else {
                return soundTypeMap.get(in.nextString());
            }
        }
    }
}
