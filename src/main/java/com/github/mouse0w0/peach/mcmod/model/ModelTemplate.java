package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;

import java.util.List;
import java.util.Map;

public class ModelTemplate {
    private Identifier id;
    private Identifier item;
    private List<String> groups;
    private List<String> textures;
    private Map<String, ModelEntry> models;

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
}
