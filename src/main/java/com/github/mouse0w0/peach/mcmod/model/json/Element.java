package com.github.mouse0w0.peach.mcmod.model.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.joml.Vector3f;

import java.lang.reflect.Type;
import java.util.Map;

public class Element {
    private Vector3f from;
    private Vector3f to;
    private Rotation rotation;
    private boolean shade = true;
    private Map<String, Face> faces;

    public static class Rotation {
        private Vector3f origin;
        private String axis;
        private float angle;
        private boolean rescale = false;

        public Vector3f getOrigin() {
            return origin;
        }

        public void setOrigin(Vector3f origin) {
            this.origin = origin;
        }

        public String getAxis() {
            return axis;
        }

        public void setAxis(String axis) {
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
                root.addProperty("axis", src.getAxis());
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

    public Map<String, Face> getFaces() {
        return faces;
    }

    public void setFaces(Map<String, Face> faces) {
        this.faces = faces;
    }

    public static class Serializer implements JsonSerializer<Element> {

        @Override
        public JsonElement serialize(Element src, Type typeOfSrc, JsonSerializationContext context) {
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
