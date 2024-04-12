package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.IconicData;
import com.github.mouse0w0.peach.mcmod.IdMetadata;

public final class BuiltinIndexProvider extends GenericIndexProvider {
    public static final BuiltinIndexProvider INSTANCE = new BuiltinIndexProvider();

    private BuiltinIndexProvider() {
        getEntries(IndexKeys.ITEM_GROUP).add("NONE", new IconicData("NONE", AppL10n.localize("itemGroup.NONE"), IdMetadata.of("minecraft:barrier")));
        getEntries(IndexKeys.MAP_COLOR).add("INHERIT", new IconicData("INHERIT", AppL10n.localize("mapColor.inherit"), IdMetadata.AIR));
        getEntries(IndexKeys.PLANT_TYPE).add("NONE", new GameData("NONE", AppL10n.localize("plantType.NONE")));
        getEntries(IndexKeys.AI_PATH_NODE_TYPE).add("INHERIT", new GameData("INHERIT", AppL10n.localize("pathNodeType.INHERIT")));
        getEntries(IndexKeys.PUSH_REACTION).add("INHERIT", new GameData("INHERIT", AppL10n.localize("pushReaction.INHERIT")));
        getEntries(IndexKeys.TOOL_TYPE).add("NONE", new GameData("NONE", AppL10n.localize("toolType.NONE")));
    }
}
