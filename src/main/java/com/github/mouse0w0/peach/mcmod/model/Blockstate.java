package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Type;
import java.util.Map;

@JsonAdapter(Blockstate.Deserializer.class)
public class Blockstate {
    private String template;
    private Identifier item;
    private Map<String, String> models;

    public Blockstate(String template, Identifier item, Map<String, String> models) {
        this.template = template;
        this.item = item;
        this.models = models;
    }

    public String getTemplate() {
        return template;
    }

    public Identifier getItem() {
        return item;
    }

    public Map<String, String> getModels() {
        return models;
    }

    public static final class Deserializer implements JsonDeserializer<Blockstate> {

        private static final Type STRING_TO_STRING_MAP = TypeUtils.parameterize(Map.class, String.class, String.class);

        @Override
        public Blockstate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject jo = json.getAsJsonObject();
                String template = context.deserialize(jo.get("template"), String.class);
                Identifier item = context.deserialize(jo.get("item"), Identifier.class);
                Map<String, String> models = context.deserialize(jo.get("models"), STRING_TO_STRING_MAP);
                return new Blockstate(template, item, models);
            }
            return null;
        }
    }
}
