package com.github.mouse0w0.peach.mcmod.model;

public class ModelEntry {
    private String name;
    private String template;
    private String parent;
    private boolean itemModel;

    public ModelEntry() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isItemModel() {
        return itemModel;
    }

    public void setItemModel(boolean itemModel) {
        this.itemModel = itemModel;
    }
}
