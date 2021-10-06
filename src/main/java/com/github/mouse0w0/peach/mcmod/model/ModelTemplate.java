package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;

import java.util.List;
import java.util.Map;

public class ModelTemplate {
    private Identifier identifier;
    private String group;
    private Identifier blockState;
    private List<String> textures;
    private Map<String, ModelEntry> blocks;
    private List<ModelEntry> items;

    public ModelTemplate() {
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Identifier getBlockState() {
        return blockState;
    }

    public void setBlockState(Identifier blockState) {
        this.blockState = blockState;
    }

    public List<String> getTextures() {
        return textures;
    }

    public void setTextures(List<String> textures) {
        this.textures = textures;
    }

    public Map<String, ModelEntry> getBlocks() {
        return blocks;
    }

    public void setBlocks(Map<String, ModelEntry> blocks) {
        this.blocks = blocks;
    }

    public List<ModelEntry> getItems() {
        return items;
    }

    public void setItems(List<ModelEntry> items) {
        this.items = items;
    }
}
