package com.github.mouse0w0.peach.mcmod.model.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class JsonModel {
    private String parent;
    @SerializedName("ambientocclusion")
    private boolean ambientOcclusion;
    private Map<String, Transform> display;
    private Map<String, String> textures;
    private List<Element> elements;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isAmbientOcclusion() {
        return ambientOcclusion;
    }

    public void setAmbientOcclusion(boolean ambientOcclusion) {
        this.ambientOcclusion = ambientOcclusion;
    }

    public Map<String, Transform> getDisplay() {
        return display;
    }

    public void setDisplay(Map<String, Transform> display) {
        this.display = display;
    }

    public Map<String, String> getTextures() {
        return textures;
    }

    public void setTextures(Map<String, String> textures) {
        this.textures = textures;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
