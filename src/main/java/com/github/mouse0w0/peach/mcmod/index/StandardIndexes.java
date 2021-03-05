package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemGroup;
import com.github.mouse0w0.peach.mcmod.ItemRef;

import java.util.List;

public interface StandardIndexes {
    LinkedMapIndex<ItemRef, List<Item>> ITEMS = new LinkedMapIndex<>("items");
    LinkedMapIndex<String, ItemGroup> ITEM_GROUPS = new LinkedMapIndex<>("item_groups");
}
