package com.github.mouse0w0.peach.mcmod.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonAdapter(ModelOverride.Persister.class)
public class ModelOverride {
    private String model;
    private List<Predicate> predicate;

    public ModelOverride() {
    }

    public ModelOverride(String model, List<Predicate> predicate) {
        this.model = model;
        this.predicate = predicate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Predicate> getPredicate() {
        return predicate;
    }

    public void setPredicate(List<Predicate> predicate) {
        this.predicate = predicate;
    }

    public static class Predicate {
        private String property;
        private float value;

        public Predicate() {
        }

        public Predicate(String property, float value) {
            this.property = property;
            this.value = value;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }

    public static class Persister implements JsonDeserializer<ModelOverride>, JsonSerializer<ModelOverride> {

        @Override
        public ModelOverride deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonOverride = json.getAsJsonObject();
            String model = jsonOverride.get("model").getAsString();

            JsonObject jsonPredicate = jsonOverride.get("predicate").getAsJsonObject();
            List<Predicate> predicate = new ArrayList<>();
            for (Map.Entry<String, JsonElement> jsonPredicateEntry : jsonPredicate.entrySet()) {
                predicate.add(new Predicate(jsonPredicateEntry.getKey(), jsonPredicateEntry.getValue().getAsFloat()));
            }
            return new ModelOverride(model, predicate);
        }

        @Override
        public JsonElement serialize(ModelOverride src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonOverride = new JsonObject();
            jsonOverride.addProperty("model", src.getModel());

            JsonObject jsonPredicate = new JsonObject();
            for (Predicate predicate : src.getPredicate()) {
                jsonOverride.addProperty(predicate.getProperty(), predicate.getValue());
            }
            jsonOverride.add("predicate", jsonPredicate);
            return jsonOverride;
        }
    }
}
