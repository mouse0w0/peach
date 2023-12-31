package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.IconicData;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ItemData;

import java.util.List;

public interface IndexTypes {
    IndexType<IdMetadata, List<ItemData>> ITEM = IndexType.of("item");
    IndexType<String, IconicData> ITEM_GROUP = IndexType.of("item_group");
    IndexType<String, GameData> SOUND_EVENT = IndexType.of("sound_event");
    IndexType<String, GameData> ENCHANTMENT = IndexType.of("enchantment");
    IndexType<String, GameData> ATTRIBUTE = IndexType.of("attribute");
    IndexType<String, IconicData> MATERIAL = IndexType.of("material");
    IndexType<String, IconicData> SOUND_TYPE = IndexType.of("sound_type");
    IndexType<String, IconicData> MAP_COLOR = IndexType.of("map_color");
    IndexType<String, GameData> ENCHANTMENT_TYPE = IndexType.of("enchantment_type");
    IndexType<String, GameData> OFFSET_TYPE = IndexType.of("offset_type");
    IndexType<String, GameData> PATH_NODE_TYPE = IndexType.of("path_node_type");
    IndexType<String, GameData> PLANT_TYPE = IndexType.of("plant_type");
    IndexType<String, GameData> PUSH_REACTION = IndexType.of("push_reaction");
    IndexType<String, GameData> RENDER_TYPE = IndexType.of("render_type");
    IndexType<String, GameData> TOOL_TYPE = IndexType.of("tool_type");
    IndexType<String, GameData> USE_ANIMATION = IndexType.of("use_animation");
}
