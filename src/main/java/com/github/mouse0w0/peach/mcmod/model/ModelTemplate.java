package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;
import java.util.Map;

public class ModelTemplate {
    private Identifier identifier;
    private List<String> groups;
    @JsonAdapter(TextureList.Deserializer.class)
    private TextureList textures;
    private Map<String, ModelEntry> models;

    public ModelTemplate() {
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public TextureList getTextures() {
        return textures;
    }

    public void setTextures(TextureList textures) {
        this.textures = textures;
    }

    public Map<String, ModelEntry> getModels() {
        return models;
    }

    public void setModels(Map<String, ModelEntry> models) {
        this.models = models;
    }
}
