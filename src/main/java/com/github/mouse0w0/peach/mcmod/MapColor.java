package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

public class MapColor implements LocalizableWithItemIcon {
    public static final MapColor INHERIT = new MapColor("inherit", "mapColor.inherit", ItemRef.AIR) {
        @Override
        public String getLocalizedText() {
            return I18n.translate(getTranslationKey());
        }

        @Override
        public void setLocalizedText(String text) {
            throw new UnsupportedOperationException();
        }
    };

    private String id;
    private String translationKey;
    private ItemRef icon;

    private transient String localizedText;

    public MapColor(String id, String translationKey, ItemRef icon) {
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

    public static final class Persister extends TypeAdapter<MapColor> {

        private final Map<String, MapColor> mapColorMap;

        public Persister(Map<String, MapColor> mapColorMap) {
            this.mapColorMap = mapColorMap;
        }

        @Override
        public void write(JsonWriter out, MapColor value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.getId());
            }
        }

        @Override
        public MapColor read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return INHERIT;
            } else {
                return mapColorMap.get(in.nextString());
            }
        }
    }
}

