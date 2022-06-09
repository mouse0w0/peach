package com.github.mouse0w0.peach.mcmod.model;

public class ModelEntry {
    private String name;
    private String template;
    private String parent;

    public ModelEntry() {
    }

    public ModelEntry(String name, String template, String parent) {
        this.name = name;
        this.template = template;
        this.parent = parent;
    }

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
