package com.github.mouse0w0.peach.mcmod.model.json;

import com.google.gson.*;
import org.joml.Vector3f;

import java.lang.reflect.Type;
import java.util.Objects;

public class Transform {

    public static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    public static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    public static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

    private Vector3f translation;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform() {
        this(TRANSLATION_DEFAULT, ROTATION_DEFAULT, SCALE_DEFAULT);
    }

    public Transform(Vector3f translation, Vector3f rotation, Vector3f scale) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transform transform = (Transform) o;
        return translation.equals(transform.translation) &&
                rotation.equals(transform.rotation) &&
                scale.equals(transform.scale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translation, rotation, scale);
    }

    public static class Serializer implements JsonSerializer<Transform> {

        private static final Transform TRANSFORM_DEFAULT = new Transform();

        @Override
        public JsonElement serialize(Transform src, Type typeOfSrc, JsonSerializationContext context) {
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
