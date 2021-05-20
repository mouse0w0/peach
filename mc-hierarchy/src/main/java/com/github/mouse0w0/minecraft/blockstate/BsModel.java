package com.github.mouse0w0.minecraft.blockstate;

import com.google.gson.annotations.SerializedName;

public class BsModel {
    private String model;
    private int x;
    private int y;
    @SerializedName("uvlock")
    private boolean uvLock;
    private int weight = 1;

    public BsModel() {
    }

    public BsModel(BsModel other) {
        this.model = other.model;
        this.x = other.x;
        this.y = other.y;
        this.uvLock = other.uvLock;
        this.weight = other.weight;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isUvLock() {
        return uvLock;
    }

    public void setUvLock(boolean uvLock) {
        this.uvLock = uvLock;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "BsModel{" +
                "model='" + model + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", uvLock=" + uvLock +
                ", weight=" + weight +
                '}';
    }
}
