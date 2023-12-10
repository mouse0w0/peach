package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.*;

import java.util.List;

public interface IndexTypes {
    IndexType<ItemRef, List<Item>> ITEMS = IndexType.of("items");
    IndexType<String, ItemGroup> ITEM_GROUPS = IndexType.of("item_groups");
    IndexType<String, Material> MATERIALS = IndexType.of("materials");
    IndexType<String, SoundType> SOUND_TYPES = IndexType.of("sound_types");
    IndexType<String, MapColor> MAP_COLORS = IndexType.of("map_colors");
    IndexType<String, SoundEvent> SOUND_EVENTS = IndexType.of("sound_events");
}
