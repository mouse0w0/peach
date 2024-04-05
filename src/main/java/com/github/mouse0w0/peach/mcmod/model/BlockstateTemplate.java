package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.plugin.Plugin;

import java.util.Map;

public class BlockstateTemplate {
    private String template;
    private Identifier item;
    private Map<String, String> models;

    private transient Plugin plugin;

    public String getTemplate() {
        return template;
    }

    public Identifier getItem() {
        return item;
    }

    public Map<String, String> getModels() {
        return models;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}
