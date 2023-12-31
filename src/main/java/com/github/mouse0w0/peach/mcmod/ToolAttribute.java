package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.index.Index;

import java.util.Objects;

public class ToolAttribute {
    public static final ToolAttribute[] EMPTY_ARRAY = new ToolAttribute[0];

    private final String type;
    private final int level;

    public ToolAttribute(String type, int level) {
        this.type = type;
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public String toLocalizedText(Index<String, GameData> toolTypeIndex) {
        GameData toolTypeData = toolTypeIndex.get(type);
        return AppL10n.localize("toolAttribute.text", toolTypeData != null ? toolTypeData.getName() : type, level);
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
        return "ToolAttribute{" +
                "type='" + type + '\'' +
                ", level=" + level +
                '}';
    }
}
