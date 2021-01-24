package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;

import java.util.List;

public interface StandardIndexes {
    LinkedMapIndex<ItemRef, List<ItemData>> ITEMS = new LinkedMapIndex<>("ITEMS");
    LinkedMapIndex<String, ItemGroupData> ITEM_GROUPS = new LinkedMapIndex<>("ITEM_GROUPS");
}
