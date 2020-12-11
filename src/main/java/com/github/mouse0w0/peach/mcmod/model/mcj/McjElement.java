package com.github.mouse0w0.peach.mcmod.model.mcj;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.joml.Vector3f;

import java.lang.reflect.Type;
import java.util.Map;

public class McjElement {
    private Vector3f from;
    private Vector3f to;
    private Rotation rotation;
    private boolean shade = true;
    private Map<McjFacing, McjFace> faces;

    public static class Rotation {
        private Vector3f origin;
        private McjAxis axis;
        private float angle;
        private boolean rescale = false;

        public Rotation() {
        }

        public Rotation(Vector3f origin, McjAxis axis, float angle, boolean rescale) {
            this.origin = origin;
            this.axis = axis;
            this.angle = angle;
            this.rescale = rescale;
        }

        public Vector3f getOrigin() {
            return origin;
        }

        public void setOrigin(Vector3f origin) {
            this.origin = origin;
        }

        public McjAxis getAxis() {
            return axis;
        }

        public void setAxis(McjAxis axis) {
            this.axis = axis;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }

        public boolean isRescale() {
            return rescale;
        }

        public void setRescale(boolean rescale) {
            this.rescale = rescale;
        }

        public static class Serializer implements JsonSerializer<Rotation> {

            @Override
            public JsonElement serialize(Rotation src, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject root = new JsonObject();
                root.add("origin", context.serialize(src.getOrigin()));
                root.add("axis", context.serialize(src.getAxis()));
                root.addProperty("angle", src.getAngle());
                if (src.isRescale()) root.addProperty("rescale", src.isRescale());
                return root;
            }
        }
    }

    public Vector3f getFrom() {
        return from;
    }

    public void setFrom(Vector3f from) {
        this.from = from;
    }

    public Vector3f getTo() {
        return to;
    }

    public void setTo(Vector3f to) {
        this.to = to;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public boolean isShade() {
        return shade;
    }

    public void setShade(boolean shade) {
        this.shade = shade;
    }

    public Map<McjFacing, McjFace> getFaces() {
        return faces;
    }

    public void setFaces(Map<McjFacing, McjFace> faces) {
        this.faces = faces;
    }

    public static class Serializer implements JsonSerializer<McjElement> {

        @Override
        public JsonElement serialize(McjElement src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject root = new JsonObject();
            root.add("from", context.serialize(src.getFrom()));
            root.add("to", context.serialize(src.getTo()));
            if (src.getRotation() != null) root.add("rotation", context.serialize(src.getRotation()));
            if (!src.isShade()) root.addProperty("shade", src.isShade());
            if (src.getFaces() != null && !src.getFaces().isEmpty())
                root.add("faces", context.serialize(src.getFaces()));
            return root;
        }
    }
}
