package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class ModelTemplate {
    private Identifier id;
    private String name;
    private List<String> blockstates;
    private List<String> textures;
    private Map<String, ModelEntry> models;

    private transient Plugin plugin;
    private transient String localizedName;

    public Identifier getId() {
        return id;
    }

    public List<String> getBlockstates() {
        return blockstates;
    }

    public List<String> getTextures() {
        return textures;
    }

    public Map<String, ModelEntry> getModels() {
        return models;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }
}
