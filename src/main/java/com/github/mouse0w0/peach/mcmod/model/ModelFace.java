package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Direction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.joml.Vector4f;

import java.lang.reflect.Type;

@JsonAdapter(ModelFace.Serializer.class)
public class ModelFace {
    private Vector4f uv;
    private String texture;
    @SerializedName("cullface")
    private Direction cullFace = null;
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

    public Direction getCullFace() {
        return cullFace;
    }

    public void setCullFace(Direction cullFace) {
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

    public static class Serializer implements JsonSerializer<ModelFace> {

        @Override
        public JsonElement serialize(ModelFace src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonFace = new JsonObject();
            jsonFace.addProperty("texture", src.getTexture());
            jsonFace.add("uv", context.serialize(src.getUv()));
            if (src.getCullFace() != null) jsonFace.add("cullface", context.serialize(src.getCullFace()));
            if (src.getRotation() != 0) jsonFace.addProperty("rotation", src.getRotation());
            if (src.getTintIndex() != -1) jsonFace.addProperty("tintindex", src.getTintIndex());
            return jsonFace;
        }
    }
}
