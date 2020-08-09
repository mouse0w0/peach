package com.github.mouse0w0.peach.mcmod.content;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.contentPack.ContentPack;
import com.github.mouse0w0.peach.mcmod.contentPack.data.ItemData;
import com.github.mouse0w0.peach.mcmod.contentPack.data.ItemGroupData;
import com.github.mouse0w0.peach.project.Project;

import java.util.*;

public class ContentManager {

    private final Map<String, ContentPack> contentPackMap = new LinkedHashMap<>();

    private final Map<Item, List<ItemData>> itemMap = new LinkedHashMap<>();
    private final Map<String, ItemGroupData> itemGroupMap = new LinkedHashMap<>();

    public static ContentManager getInstance(Project project) {
        return project.getService(ContentManager.class);
    }

    public void addContentPack(ContentPack contentPack) {
        if (contentPackMap.containsKey(contentPack.getId())) return;
        contentPackMap.put(contentPack.getId(), contentPack);
        contentPack.getItemData().forEach(itemData -> {
            getOrCreateItemData(Item.createItem(itemData.getId(), itemData.getMetadata())).add(itemData);
            getOrCreateItemData(Item.createIgnoreMetadata(itemData.getId())).add(itemData);
        });
        contentPack.getOreDictionaryData().forEach(oreDictData -> {
            List<ItemData> itemData = getOrCreateItemData(Item.createOreDict(oreDictData.getId()));
            oreDictData.getEntries().forEach(itemToken -> itemData.addAll(getItemData(itemToken)));
        });
        contentPack.getItemGroupData().forEach(itemGroupData -> itemGroupMap.put(itemGroupData.getId(), itemGroupData));
    }

    public Map<Item, List<ItemData>> getItemMap() {
        return itemMap;
    }

    private List<ItemData> getOrCreateItemData(Item item) {
        return itemMap.computeIfAbsent(item, $ -> new ArrayList<>(1));
    }

    public List<ItemData> getItemData(Item item) {
        List<ItemData> itemData = itemMap.get(item);
        return itemData != null ? itemData : Collections.emptyList();
    }

    public Map<String, ItemGroupData> getItemGroupMap() {
        return itemGroupMap;
    }

    public ItemGroupData getItemGroup(String id) {
        return itemGroupMap.get(id);
    }
}
