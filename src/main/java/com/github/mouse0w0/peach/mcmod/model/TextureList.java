package com.github.mouse0w0.peach.mcmod.model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TextureList extends ArrayList<TextureEntry> {

    private boolean customLayout;

    public boolean isCustomLayout() {
        return customLayout;
    }

    public void setCustomLayout(boolean customLayout) {
        this.customLayout = customLayout;
    }

    public static final class Deserializer implements JsonDeserializer<TextureList> {

        @Override
        public TextureList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonArray()) {
                JsonArray jTextures = json.getAsJsonArray();
                TextureList textureList = new TextureList();
                for (JsonElement jTexture : jTextures) {
                    textureList.add(new TextureEntry(jTexture.getAsString()));
                }
                return textureList;
            } else if (json.isJsonObject()) {
                JsonObject jTextures = json.getAsJsonObject();
                TextureList textureList = new TextureList();
                boolean customLayout = false;
                for (String key : jTextures.keySet()) {
                    JsonObject jValue = jTextures.getAsJsonObject(key);

                    int column = -1, row = -1;
                    if (jValue.has("column") && jValue.has("row")) {
                        customLayout = true;
                        column = jValue.get("column").getAsInt();
                        row = jValue.get("row").getAsInt();
                    }

                    String translationKey = null;
                    if (jValue.has("translationKey")) {
                        translationKey = jValue.get("translationKey").getAsString();
                    }

                    textureList.add(new TextureEntry(key, column, row, translationKey));
                }
                textureList.setCustomLayout(customLayout);
                return textureList;
            }
            return null;
        }
    }
}
