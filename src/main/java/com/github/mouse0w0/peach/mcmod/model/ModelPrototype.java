package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;

import java.util.List;
import java.util.Map;

public class ModelPrototype {
    private Identifier id;
    private Identifier item;
    private List<String> groups;
    private List<String> textures;
    private Map<String, ModelEntry> models;

    public ModelPrototype() {
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public Identifier getItem() {
        return item;
    }

    public void setItem(Identifier item) {
        this.item = item;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getTextures() {
        return textures;
    }

    public void setTextures(List<String> textures) {
        this.textures = textures;
    }

    public Map<String, ModelEntry> getModels() {
        return models;
    }

    public void setModels(Map<String, ModelEntry> models) {
        this.models = models;
    }
}
