package com.github.mouse0w0.minecraft.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;
import org.joml.Vector4f;

import java.lang.reflect.Type;

public class McFace {
    private Vector4f uv;
    private String texture;
    @SerializedName("cullface")
    private String cullFace = null;
    private int rotation = 0;
    @SerializedName("tintindex")
    private int tintIndex = -1;

    public Vector4f getUv() {
        return uv;
    }

    public void setUv(Vector4f uv) {
        this.uv = uv;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getCullFace() {
        return cullFace;
    }

    public void setCullFace(String cullFace) {
        this.cullFace = cullFace;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getTintIndex() {
        return tintIndex;
    }

    public void setTintIndex(int tintIndex) {
        this.tintIndex = tintIndex;
    }

    public static class Serializer implements JsonSerializer<McFace> {

        @Override
        public JsonElement serialize(McFace src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject root = new JsonObject();
            root.addProperty("texture", src.getTexture());
            root.add("uv", context.serialize(src.getUv()));
            if (src.getCullFace() != null) root.addProperty("cullface", src.getCullFace());
            if (src.getRotation() != 0) root.addProperty("rotation", src.getRotation());
            if (src.getTintIndex() != -1) root.addProperty("tintindex", src.getTintIndex());
            return root;
        }
    }
}
