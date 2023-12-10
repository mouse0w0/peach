package com.github.mouse0w0.peach.mcmod.content;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.index.GenericIndexProvider;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexTypes;
import com.github.mouse0w0.peach.project.Project;

import java.util.*;

public final class ProjectContentPackManager extends GenericIndexProvider {
    private final Map<String, ContentPack> contentPacks = new LinkedHashMap<>();

    public static ProjectContentPackManager getInstance(Project project) {
        return project.getService(ProjectContentPackManager.class);
    }

    public ProjectContentPackManager(IndexManager indexManager) {
        super("library", 5000);

        addContentPacks(ContentPackManager.getInstance().getContentPacks());

        indexManager.addProvider(this);
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

        Map<String, ItemGroup> itemGroupIndex = getIndex(IndexTypes.ITEM_GROUPS);
        contentPack.getData(ItemGroup.class).forEach(itemGroup -> itemGroupIndex.put(itemGroup.getId(), itemGroup));

        Map<String, Material> materialIndex = getIndex(IndexTypes.MATERIALS);
        contentPack.getData(Material.class).forEach(material -> materialIndex.put(material.getId(), material));

        Map<String, SoundType> soundTypeIndex = getIndex(IndexTypes.SOUND_TYPES);
        contentPack.getData(SoundType.class).forEach(soundType -> soundTypeIndex.put(soundType.getId(), soundType));

        Map<String, MapColor> mapColorIndex = getIndex(IndexTypes.MAP_COLORS);
        contentPack.getData(MapColor.class).forEach(mapColor -> mapColorIndex.put(mapColor.getId(), mapColor));

        Map<String, SoundEvent> soundEventIndex = getIndex(IndexTypes.SOUND_EVENTS);
        contentPack.getData(SoundEvent.class).forEach(soundEvent -> soundEventIndex.put(soundEvent.getId(), soundEvent));
    }

    private void addItem(Item itemData) {
        ItemRef item = ItemRef.createItem(itemData.getId(), itemData.getMetadata());
        getItemDataList(item).add(itemData);
        ItemRef ignoreMetadata = ItemRef.createIgnoreMetadata(itemData.getId());
        getItemDataList(ignoreMetadata).add(itemData);
    }

    private void addOreDict(OreDict oreDict) {
        List<Item> itemData = getItemDataList(ItemRef.createOreDictionary(oreDict.getId()));
        oreDict.getEntries().forEach(itemRef -> itemData.addAll(getIndex(IndexTypes.ITEMS).get(itemRef)));
    }

    private List<Item> getItemDataList(ItemRef item) {
        return getIndex(IndexTypes.ITEMS).computeIfAbsent(item, k -> new ArrayList<>(1));
    }
}
