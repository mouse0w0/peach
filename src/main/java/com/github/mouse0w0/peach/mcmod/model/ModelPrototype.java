package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@JsonAdapter(ModelPrototype.Deserializer.class)
public class ModelPrototype {
    private Identifier id;
    private Identifier item;
    private List<String> groups;
    private List<String> textures;
    private Map<String, ModelEntry> models;

    public ModelPrototype() {
    }

    public ModelPrototype(Identifier id, Identifier item, List<String> groups, List<String> textures, Map<String, ModelEntry> models) {
        this.id = id;
        this.item = item;
        this.groups = groups;
        this.textures = textures;
        this.models = models;
    }

    public Identifier getId() {
        return id;
    }

    public Identifier getItem() {
        return item;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<String> getTextures() {
        return textures;
    }

    public Map<String, ModelEntry> getModels() {
        return models;
    }

    static final class Deserializer implements JsonDeserializer<ModelPrototype> {

        private static final Type STRING_LIST = TypeUtils.parameterize(List.class, String.class);
        private static final Type STRING_TO_MODEL_ENTRY_MAP = TypeUtils.parameterize(Map.class, String.class, ModelEntry.class);

        @Override
        public ModelPrototype deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject jo = json.getAsJsonObject();
                Identifier id = context.deserialize(jo.get("id"), Identifier.class);
                Identifier item = context.deserialize(jo.get("item"), Identifier.class);
                List<String> groups = context.deserialize(jo.get("groups"), STRING_LIST);
                List<String> textures = context.deserialize(jo.get("textures"), STRING_LIST);
                Map<String, ModelEntry> models = context.deserialize(jo.get("models"), STRING_TO_MODEL_ENTRY_MAP);
                return new ModelPrototype(id, item, groups, textures, models);
            }
            return null;
        }
    }
}
