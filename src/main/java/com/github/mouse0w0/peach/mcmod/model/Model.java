package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@JsonAdapter(Model.Serializer.class)
public class Model {
    private Identifier parent;
    @SerializedName("ambientocclusion")
    private Boolean ambientOcclusion = true;
    @SerializedName("gui_light")
    private String guiLight;
    private Map<String, ModelTransform> display;
    private Map<String, String> textures;
    private List<ModelElement> elements;
    private List<ModelOverride> overrides;

    private transient Model parentModel;

    public Identifier getParent() {
        return parent;
    }

    public void setParent(Identifier parent) {
        this.parent = parent;
    }

    public Boolean hasAmbientOcclusion() {
        return ambientOcclusion;
    }

    public void setAmbientOcclusion(Boolean ambientOcclusion) {
        this.ambientOcclusion = ambientOcclusion;
    }

    public String getGuiLight() {
        return guiLight;
    }

    public void setGuiLight(String guiLight) {
        this.guiLight = guiLight;
    }

    public Map<String, ModelTransform> getDisplay() {
        return display;
    }

    public void setDisplay(Map<String, ModelTransform> display) {
        this.display = display;
    }

    public Map<String, String> getTextures() {
        return textures;
    }

    public void setTextures(Map<String, String> textures) {
        this.textures = textures;
    }

    public List<ModelElement> getElements() {
        return elements;
    }

    public void setElements(List<ModelElement> elements) {
        this.elements = elements;
    }

    public List<ModelOverride> getOverrides() {
        return overrides;
    }

    public void setOverrides(List<ModelOverride> overrides) {
        this.overrides = overrides;
    }

    public Model getParentModel() {
        return parentModel;
    }

    public void setParentModel(Model parentModel) {
        this.parentModel = parentModel;
    }

    public boolean resolveHasAmbientOcclusion() {
        for (Model model = this; model != null; model = model.getParentModel()) {
            Boolean ambientOcclusion = model.hasAmbientOcclusion();
            if (ambientOcclusion != null) {
                return ambientOcclusion;
            }
        }
        return true;
    }

    @Nullable
    public String resolveGuiLight() {
        for (Model model = this; model != null; model = model.getParentModel()) {
            String guiLight = model.getGuiLight();
            if (guiLight != null) {
                return guiLight;
            }
        }
        return null;
    }

    public String resolveTexture(String texture) {
        if (texture.charAt(0) == '#') {
            texture = texture.substring(1);
        }

        while (true) {
            String result = findTexture(texture);
            if (result == null) {
                return null;
            } else if (result.charAt(0) == '#') {
                texture = result.substring(1);
            } else {
                return result;
            }
        }
    }

    private String findTexture(String texture) {
        for (Model model = this; model != null; model = model.getParentModel()) {
            Map<String, String> textures = model.getTextures();
            if (textures == null) continue;

            String result = textures.get(texture);
            if (result != null) return result;
        }
        return null;
    }

    @Nullable
    public List<ModelElement> resolveElements() {
        for (Model model = this; model != null; model = model.getParentModel()) {
            List<ModelElement> elements = model.getElements();
            if (elements != null) {
                return elements;
            }
        }
        return null;
    }

    @Nullable
    public ModelTransform resolveTransform(String transformName) {
        for (Model model = this; model != null; model = model.getParentModel()) {
            Map<String, ModelTransform> display = model.getDisplay();
            if (display != null) {
                ModelTransform transform = display.get(transformName);
                if (transform != null) {
                    return transform;
                }
            }
        }
        return null;
    }

    public static class Serializer implements JsonSerializer<Model> {
        @Override
        public JsonElement serialize(Model src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonModel = new JsonObject();
            if (src.getParent() != null) jsonModel.add("parent", context.serialize(src.getParent()));
            if (!src.hasAmbientOcclusion()) jsonModel.addProperty("ambientocclusion", src.hasAmbientOcclusion());
            if (src.getGuiLight() != null) jsonModel.addProperty("gui_light", src.getGuiLight());
            if (src.getDisplay() != null && !src.getDisplay().isEmpty())
                jsonModel.add("display", context.serialize(src.getDisplay()));
            if (src.getTextures() != null && !src.getTextures().isEmpty())
                jsonModel.add("textures", context.serialize(src.getTextures()));
            if (src.getElements() != null && !src.getElements().isEmpty())
                jsonModel.add("elements", context.serialize(src.getElements()));
            if (src.getOverrides() != null && !src.getOverrides().isEmpty())
                jsonModel.add("overrides", context.serialize(src.getOverrides()));
            return jsonModel;
        }
    }
}
