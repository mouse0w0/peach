package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public final class ToolType {
    private static final Set<String> TOOL_TYPES;

    public static final String AXE = "axe";
    public static final String PICKAXE = "pickaxe";
    public static final String SHOVEL = "shovel";
    public static final String HOE = "hoe";

    static {
        TOOL_TYPES = ImmutableSet.of(AXE, PICKAXE, SHOVEL, HOE);
    }

    public static Set<String> getToolTypes() {
        return TOOL_TYPES;
    }

    public static String getLocalizedName(String toolType) {
        return I18n.translate("toolType." + toolType);
    }

    private ToolType() {
    }
}
