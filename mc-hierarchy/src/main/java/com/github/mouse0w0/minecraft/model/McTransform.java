package com.github.mouse0w0.minecraft.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.lang.reflect.Type;
import java.util.Objects;

@JsonAdapter(McTransform.Serializer.class)
public class McTransform {

    public static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    public static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    public static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

    private Vector3f translation;
    private Vector3f rotation;
    private Vector3f scale;

    public McTransform() {
        this(TRANSLATION_DEFAULT, ROTATION_DEFAULT, SCALE_DEFAULT);
    }

    public McTransform(Vector3f translation, Vector3f rotation, Vector3f scale) {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Matrix4f getMatrix() {
        return getMatrix(new Matrix4f());
    }

    public Matrix4f getMatrix(Matrix4f dest) {
        return dest.identity()
                .rotateX(rotation.x())
                .rotateY(rotation.y())
                .rotateZ(rotation.z())
                .translate(translation)
                .scale(scale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        McTransform transform = (McTransform) o;
        return translation.equals(transform.translation) &&
                rotation.equals(transform.rotation) &&
                scale.equals(transform.scale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translation, rotation, scale);
    }

    public static class Serializer implements JsonSerializer<McTransform> {

        private static final McTransform TRANSFORM_DEFAULT = new McTransform();

        @Override
        public JsonElement serialize(McTransform src, Type typeOfSrc, JsonSerializationContext context) {
            if (src.equals(TRANSFORM_DEFAULT)) return JsonNull.INSTANCE;

            JsonObject root = new JsonObject();
            if (!TRANSLATION_DEFAULT.equals(src.getTranslation()))
                root.add("translation", context.serialize(src.getTranslation()));
            if (!ROTATION_DEFAULT.equals(src.getRotation()))
                root.add("rotation", context.serialize(src.getRotation()));
            if (!SCALE_DEFAULT.equals(src.getScale()))
                root.add("scale", context.serialize(src.getScale()));
            return root;
        }
    }
}
