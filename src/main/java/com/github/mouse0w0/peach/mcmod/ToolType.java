package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.google.common.collect.ImmutableList;

import java.util.List;

public final class ToolType {
    private static final List<String> TOOL_TYPES;

    public static final String NONE = "none";
    public static final String AXE = "axe";
    public static final String PICKAXE = "pickaxe";
    public static final String SHOVEL = "shovel";
    public static final String HOE = "hoe";

    static {
        TOOL_TYPES = ImmutableList.of(AXE, PICKAXE, SHOVEL, HOE);
    }

    public static List<String> getToolTypes() {
        return TOOL_TYPES;
    }

    public static String getLocalizedName(String toolType) {
        return AppL10n.localize("toolType." + toolType);
    }

    private ToolType() {
    }
}
