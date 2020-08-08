package com.github.mouse0w0.peach.mcmod.model.json;

import com.google.gson.annotations.SerializedName;
import org.joml.Vector3i;
import org.joml.Vector4i;

import java.util.Map;

public class Element {
    private Vector3i from;
    private Vector3i to;
    private Rotation rotation;
    private boolean shade;
    private Map<String, Face> faces;

    public static class Rotation {
        private Vector3i origin;
        private String axis;
        private double angle;
        private boolean rescale;

        public Vector3i getOrigin() {
            return origin;
        }

        public void setOrigin(Vector3i origin) {
            this.origin = origin;
        }

        public String getAxis() {
            return axis;
        }

        public void setAxis(String axis) {
            this.axis = axis;
        }

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {
            this.angle = angle;
        }

        public boolean isRescale() {
            return rescale;
        }

        public void setRescale(boolean rescale) {
            this.rescale = rescale;
        }
    }

    public static class Face {
        private Vector4i uv;
        private String texture;
        @SerializedName("cullface")
        private String cullFace;
        private int rotation;
        @SerializedName("tintindex")
        private int tintIndex;

        public Vector4i getUv() {
            return uv;
        }

        public void setUv(Vector4i uv) {
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
    }

    public Vector3i getFrom() {
        return from;
    }

    public void setFrom(Vector3i from) {
        this.from = from;
    }

    public Vector3i getTo() {
        return to;
    }

    public void setTo(Vector3i to) {
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
}
