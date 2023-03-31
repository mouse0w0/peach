package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.google.common.base.CaseFormat;

public enum BlockType implements Localizable {
    NORMAL("normal"),
    PILLAR("pillar"),
    HORIZONTAL("horizontal"),
    HORIZONTAL_OPPOSITE("horizontal"),
    DIRECTIONAL("directional"),
    DIRECTIONAL_OPPOSITE("directional"),
    DIRT("dirt"),
    STONE("stone"),
    STAIRS("stairs"),
    SLAB("slab"),
    FENCE("fence"),
    FENCE_GATE("fence_gate"),
    WALL("wall"),
    TRAPDOOR("trapdoor"),
    PANE("pane");

    private final String blockstate;
    private final String translationKey;

    BlockType(String blockstate) {
        this.blockstate = blockstate;
        translationKey = "blockType." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

    public String getBlockstate() {
        return blockstate;
    }

    public String getLocalizedText() {
        return AppL10n.localize(translationKey);
    }
}
