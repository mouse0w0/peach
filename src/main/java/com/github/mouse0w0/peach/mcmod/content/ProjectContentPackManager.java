package com.github.mouse0w0.peach.mcmod.content;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.project.Project;

import java.util.*;

public final class ProjectContentPackManager extends IndexProvider {
    private final Map<String, ContentPack> contentPacks = new LinkedHashMap<>();

    public static ProjectContentPackManager getInstance(Project project) {
        return project.getService(ProjectContentPackManager.class);
    }

    public ProjectContentPackManager(IndexManager indexManager) {
        super("LIBRARY", 100);

        addContentPacks(ContentPackManager.getInstance().getContentPacks());

        indexManager.registerProvider(this);
    }

    public void addContentPacks(Collection<ContentPack> contentPacks) {
        for (ContentPack contentPack : contentPacks) {
            addContentPack(contentPack);
        }
    }

    public void addContentPack(ContentPack contentPack) {
        if (contentPacks.containsKey(contentPack.getId())) return;
        contentPacks.put(contentPack.getId(), contentPack);
        contentPack.getData(Item.class).forEach(this::addItem);
        contentPack.getData(OreDict.class).forEach(this::addOreDict);

        Map<String, ItemGroup> itemGroupIndex = getIndex(Indexes.ITEM_GROUPS);
        contentPack.getData(ItemGroup.class).forEach(itemGroup -> itemGroupIndex.put(itemGroup.getId(), itemGroup));

        Map<String, Material> materialIndex = getIndex(Indexes.MATERIALS);
        contentPack.getData(Material.class).forEach(material -> materialIndex.put(material.getId(), material));

        Map<String, SoundType> soundTypeIndex = getIndex(Indexes.SOUND_TYPES);
        contentPack.getData(SoundType.class).forEach(soundType -> soundTypeIndex.put(soundType.getId(), soundType));

        Map<String, MapColor> mapColorIndex = getIndex(Indexes.MAP_COLORS);
        contentPack.getData(MapColor.class).forEach(mapColor -> mapColorIndex.put(mapColor.getId(), mapColor));

        Map<String, SoundEvent> soundEventIndex = getIndex(Indexes.SOUND_EVENTS);
        contentPack.getData(SoundEvent.class).forEach(soundEvent -> soundEventIndex.put(soundEvent.getId(), soundEvent));
    }

    private void addItem(Item itemData) {
        ItemRef item = ItemRef.createItem(itemData.getId(), itemData.getMetadata());
        getItemDataList(item).add(itemData);
        ItemRef ignoreMetadata = ItemRef.createIgnoreMetadata(itemData.getId());
        getItemDataList(ignoreMetadata).add(itemData);
    }

    private void addOreDict(OreDict oreDict) {
        List<Item> itemData = getItemDataList(ItemRef.createOreDict(oreDict.getId()));
        oreDict.getEntries().forEach(itemRef -> itemData.addAll(getIndex(Indexes.ITEMS).get(itemRef)));
    }

    private List<Item> getItemDataList(ItemRef item) {
        return getIndex(Indexes.ITEMS).computeIfAbsent(item, k -> new ArrayList<>(1));
    }
}
