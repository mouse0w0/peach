package com.github.mouse0w0.peach.mcmod.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.lang.reflect.Type;
import java.util.Objects;

@JsonAdapter(ModelTransform.TypeAdapter.class)
public class ModelTransform {
    public static final ModelTransform DEFAULT = new ModelTransform(new Vector3f(), new Vector3f(), new Vector3f(1));

    private static final Vector3f TRANSLATION_DEFAULT = new Vector3f();
    private static final Vector3f ROTATION_DEFAULT = new Vector3f();
    private static final Vector3f SCALE_DEFAULT = new Vector3f(1);

    private static final float TO_RADIANS = (float) (Math.PI / 180);

    private final Vector3f translation;
    private final Vector3f rotation;
    private final Vector3f scale;

    public ModelTransform(Vector3f translation, Vector3f rotation, Vector3f scale) {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Matrix4f apply(Matrix4f dest) {
        return dest.rotateXYZ(rotation.x() * TO_RADIANS, rotation.y() * TO_RADIANS, rotation.z() * TO_RADIANS)
                .scale(scale)
                .translate(translation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelTransform transform = (ModelTransform) o;
        return translation.equals(transform.translation) &&
                rotation.equals(transform.rotation) &&
                scale.equals(transform.scale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translation, rotation, scale);
    }

    public static class TypeAdapter implements JsonDeserializer<ModelTransform>, JsonSerializer<ModelTransform> {
        @Override
        public ModelTransform deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonTransform = json.getAsJsonObject();

            JsonElement jsonTranslation = jsonTransform.get("translation");
            Vector3f translation = jsonTranslation != null ? context.deserialize(jsonTranslation, Vector3f.class) : new Vector3f();
            translation.mul(0.0625F);

            JsonElement jsonRotation = jsonTransform.get("rotation");
            Vector3f rotation = jsonRotation != null ? context.deserialize(jsonRotation, Vector3f.class) : new Vector3f();

            JsonElement jsonScale = jsonTransform.get("scale");
            Vector3f scale = jsonScale != null ? context.deserialize(jsonScale, Vector3f.class) : new Vector3f(1);

            return new ModelTransform(translation, rotation, scale);
        }

        @Override
        public JsonElement serialize(ModelTransform src, Type typeOfSrc, JsonSerializationContext context) {
            if (DEFAULT.equals(src)) return JsonNull.INSTANCE;

            JsonObject jsonTransform = new JsonObject();
            if (!TRANSLATION_DEFAULT.equals(src.getTranslation()))
                jsonTransform.add("translation", context.serialize(src.getTranslation()));
            if (!ROTATION_DEFAULT.equals(src.getRotation()))
                jsonTransform.add("rotation", context.serialize(src.getRotation()));
            if (!SCALE_DEFAULT.equals(src.getScale()))
                jsonTransform.add("scale", context.serialize(src.getScale()));
            return jsonTransform;
        }
    }
}
