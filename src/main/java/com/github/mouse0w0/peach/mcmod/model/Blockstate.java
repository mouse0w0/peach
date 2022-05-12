package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;

import java.util.List;

public class Blockstate {
    private String template;
    private Identifier item;
    private List<String> models;

    public String getTemplate() {
        return template;
    }

    public Identifier getItem() {
        return item;
    }

    public List<String> getModels() {
        return models;
    }
}
