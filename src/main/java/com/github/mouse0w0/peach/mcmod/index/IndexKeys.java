package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.IconicData;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ItemData;

import java.util.List;

public interface IndexKeys {
    IndexKey<IdMetadata, List<ItemData>> ITEM = IndexKey.of("item");
    IndexKey<String, IconicData> ITEM_GROUP = IndexKey.of("item_group");
    IndexKey<String, GameData> SOUND_EVENT = IndexKey.of("sound_event");
    IndexKey<String, GameData> ENCHANTMENT = IndexKey.of("enchantment");
    IndexKey<String, GameData> ATTRIBUTE = IndexKey.of("attribute");
    IndexKey<String, IconicData> MATERIAL = IndexKey.of("material");
    IndexKey<String, IconicData> SOUND_TYPE = IndexKey.of("sound_type");
    IndexKey<String, IconicData> MAP_COLOR = IndexKey.of("map_color");
    IndexKey<String, GameData> ENCHANTMENT_TYPE = IndexKey.of("enchantment_type");
    IndexKey<String, GameData> OFFSET_TYPE = IndexKey.of("offset_type");
    IndexKey<String, GameData> AI_PATH_NODE_TYPE = IndexKey.of("ai_path_node_type");
    IndexKey<String, GameData> PLANT_TYPE = IndexKey.of("plant_type");
    IndexKey<String, GameData> PUSH_REACTION = IndexKey.of("push_reaction");
    IndexKey<String, GameData> RENDER_TYPE = IndexKey.of("render_type");
    IndexKey<String, GameData> TOOL_TYPE = IndexKey.of("tool_type");
    IndexKey<String, GameData> USE_ANIMATION = IndexKey.of("use_animation");
}
