package com.github.mouse0w0.minecraft.model;

import com.google.gson.JsonElement;

public class McOverride {
    private JsonElement predicate;
    private String model;

    public JsonElement getPredicate() {
        return predicate;
    }

    public void setPredicate(JsonElement predicate) {
        this.predicate = predicate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
