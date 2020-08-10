package com.github.mouse0w0.peach.mcmod.model.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonModel {
    private String parent;
    @SerializedName("ambientocclusion")
    private boolean ambientOcclusion = true;
    @SerializedName("gui_light")
    private String guiLight;
    private Map<String, Transform> display;
    private Map<String, String> textures;
    private List<Element> elements;
    // TODO: overrides

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

    public String getGuiLight() {
        return guiLight;
    }

    public void setGuiLight(String guiLight) {
        this.guiLight = guiLight;
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

    public static class Serializer implements JsonSerializer<JsonModel> {

        @Override
        public JsonElement serialize(JsonModel src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject root = new JsonObject();
            if (src.getParent() != null) root.addProperty("parent", src.getParent());
            if (!src.isAmbientOcclusion()) root.addProperty("ambientocclusion", src.isAmbientOcclusion());
            if (src.getGuiLight() != null) root.addProperty("gui_light", src.getGuiLight());
            if (src.getDisplay() != null && !src.getDisplay().isEmpty())
                root.add("display", context.serialize(src.getDisplay()));
            if (src.getTextures() != null && !src.getTextures().isEmpty())
                root.add("textures", context.serialize(src.getTextures()));
            if (src.getElements() != null && !src.getElements().isEmpty())
                root.add("elements", context.serialize(src.getElements()));
            return root;
        }
    }
}
