package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.*;

import java.util.List;

public interface IndexTypes {
    IndexType<ItemRef, List<Item>> ITEM = IndexType.of("item");
    IndexType<String, ItemGroup> ITEM_GROUP = IndexType.of("item_group");
    IndexType<String, Material> MATERIAL = IndexType.of("material");
    IndexType<String, SoundType> SOUND_TYPE = IndexType.of("sound_type");
    IndexType<String, MapColor> MAP_COLOR = IndexType.of("map_color");
    IndexType<String, SoundEvent> SOUND_EVENT = IndexType.of("sound_event");
}
