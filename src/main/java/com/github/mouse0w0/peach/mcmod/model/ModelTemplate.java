package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class ModelTemplate {
    private Identifier id;
    private Identifier item;
    private List<String> blockstates;
    private List<String> textures;
    private Map<String, Entry> models;

    private transient Plugin plugin;
    private transient String localizedName;

    public Identifier getId() {
        return id;
    }

    public Identifier getItem() {
        return item;
    }

    public List<String> getBlockstates() {
        return blockstates;
    }

    public List<String> getTextures() {
        return textures;
    }

    public Map<String, Entry> getModels() {
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

    public static class Entry {
        private String name;
        private String template;
        private String parent;

        public String getName() {
            return name;
        }

        public String getTemplate() {
            return template;
        }

        public String getParent() {
            return parent;
        }
    }
}
