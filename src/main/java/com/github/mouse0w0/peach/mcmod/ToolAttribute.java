package com.github.mouse0w0.peach.mcmod;

import java.util.Objects;

public class ToolAttribute {
    public static final ToolAttribute[] EMPTY_ARRAY = new ToolAttribute[0];

    private String type;
    private int level;

    public ToolAttribute() {
    }

    public ToolAttribute(String type, int level) {
        this.type = type;
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolAttribute that = (ToolAttribute) o;
        return level == that.level && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, level);
    }

    @Override
    public String toString() {
        return "ToolDefinition{" +
                "type='" + type + '\'' +
                ", level=" + level +
                '}';
    }
}
