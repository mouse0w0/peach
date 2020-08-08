package com.github.mouse0w0.peach.mcmod.model.json;

import org.joml.Vector3d;

public class Transform {

    private Vector3d translation;
    private Vector3d rotation;
    private Vector3d scale;

    public Transform(Vector3d translation, Vector3d rotation, Vector3d scale) {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3d getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3d translation) {
        this.translation = translation;
    }

    public Vector3d getRotation() {
        return rotation;
    }

    public void setRotation(Vector3d rotation) {
        this.rotation = rotation;
    }

    public Vector3d getScale() {
        return scale;
    }

    public void setScale(Vector3d scale) {
        this.scale = scale;
    }
}
