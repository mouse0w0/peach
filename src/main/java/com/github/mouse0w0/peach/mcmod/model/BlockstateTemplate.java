package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;

import java.util.Map;

public class BlockstateTemplate {
    private String template;
    private Identifier item;
    private Map<String, String> models;

    public String getTemplate() {
        return template;
    }

    public Identifier getItem() {
        return item;
    }

    public Map<String, String> getModels() {
        return models;
    }
}
