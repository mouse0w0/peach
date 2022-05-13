package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.*;

import java.util.List;

public interface Indexes {
    LinkedMapIndex<ItemRef, List<Item>> ITEMS = new LinkedMapIndex<>("items");
    LinkedMapIndex<String, ItemGroup> ITEM_GROUPS = new LinkedMapIndex<>("item_groups");
    LinkedMapIndex<String, Material> MATERIALS = new LinkedMapIndex<>("materials");
    LinkedMapIndex<String, SoundType> SOUND_TYPES = new LinkedMapIndex<>("sound_types");
    LinkedMapIndex<String, MapColor> MAP_COLORS = new LinkedMapIndex<>("map_colors");
    LinkedMapIndex<String, SoundEvent> SOUND_EVENTS = new LinkedMapIndex<>("sound_events");
}
