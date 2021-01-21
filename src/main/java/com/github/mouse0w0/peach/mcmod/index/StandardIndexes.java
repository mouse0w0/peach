package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;

import java.util.List;

public interface StandardIndexes {
    Index<ItemRef, List<ItemData>> ITEMS = Index.of("ITEMS");
    Index<String, ItemGroupData> ITEM_GROUPS = Index.of("ITEM_GROUPS");
}
