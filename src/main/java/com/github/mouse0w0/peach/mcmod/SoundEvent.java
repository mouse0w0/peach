package com.github.mouse0w0.peach.mcmod;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

public class SoundEvent implements Localizable {
    private String id;
    private String translationKey;

    private transient String localizedText;

    public SoundEvent(String id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    public String getId() {
        return id;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getLocalizedText() {
        return localizedText;
    }

    public void setLocalizedText(String localizedText) {
        this.localizedText = localizedText;
    }

    public static final class Persister extends TypeAdapter<SoundEvent> {

        private final Map<String, SoundEvent> soundEventMap;

        public Persister(Map<String, SoundEvent> soundEventMap) {
            this.soundEventMap = soundEventMap;
        }

        @Override
        public void write(JsonWriter out, SoundEvent value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.getId());
            }
        }

        @Override
        public SoundEvent read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else {
                return soundEventMap.get(in.nextString());
            }
        }
    }
}
