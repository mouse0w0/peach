package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.IconicData;
import com.github.mouse0w0.peach.mcmod.IdMetadata;

public class BuiltinIndexProvider extends GenericIndexProvider {
    public BuiltinIndexProvider(IndexManager indexManager) {
        super("builtin", 100);

        indexManager.addProvider(this);

        getIndex(IndexTypes.ITEM_GROUP).put("NONE", new IconicData("NONE", AppL10n.localize("itemGroup.NONE"), IdMetadata.of("minecraft:barrier")));
        getIndex(IndexTypes.MAP_COLOR).put("INHERIT", new IconicData("INHERIT", AppL10n.localize("mapColor.inherit"), IdMetadata.AIR));
        getIndex(IndexTypes.PLANT_TYPE).put("NONE", new GameData("NONE", AppL10n.localize("plantType.NONE")));
        getIndex(IndexTypes.PATH_NODE_TYPE).put("INHERIT", new GameData("INHERIT", AppL10n.localize("pathNodeType.INHERIT")));
        getIndex(IndexTypes.PUSH_REACTION).put("INHERIT", new GameData("INHERIT", AppL10n.localize("pushReaction.INHERIT")));
        getIndex(IndexTypes.TOOL_TYPE).put("NONE", new GameData("NONE", AppL10n.localize("toolType.NONE")));
    }
}
