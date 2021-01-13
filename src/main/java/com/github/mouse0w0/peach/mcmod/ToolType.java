package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class ToolType {
    private static final Map<String, ToolType> TOOL_TYPES = new LinkedHashMap<>();

    public static final ToolType AXE = of("axe");
    public static final ToolType PICKAXE = of("pickaxe");
    public static final ToolType SHOVEL = of("shovel");
    public static final ToolType HOE = of("hoe");

    public static Collection<ToolType> getToolTypes() {
        return TOOL_TYPES.values();
    }

    public static ToolType of(String name) {
        return TOOL_TYPES.computeIfAbsent(name, ToolType::new);
    }

    private final String name;

    private ToolType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLocalizedName() {
        return I18n.translate("toolType." + name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolType toolType = (ToolType) o;
        return name.equals(toolType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ToolType{" +
                "name='" + name + '\'' +
                '}';
    }
}
