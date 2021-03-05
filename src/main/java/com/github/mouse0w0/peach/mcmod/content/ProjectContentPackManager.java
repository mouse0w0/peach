package com.github.mouse0w0.peach.mcmod.content;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemGroup;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.OreDict;
import com.github.mouse0w0.peach.mcmod.content.contentPack.ContentPack;
import com.github.mouse0w0.peach.mcmod.content.contentPack.ContentPackManager;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.index.StandardIndexes;
import com.github.mouse0w0.peach.project.Project;

import java.util.*;

public final class ProjectContentPackManager extends IndexProvider {
    private final Map<String, ContentPack> contentPacks = new LinkedHashMap<>();

    public static ProjectContentPackManager getInstance(Project project) {
        return project.getService(ProjectContentPackManager.class);
    }

    public ProjectContentPackManager(Project project) {
        super("LIBRARY", 100);

        addContentPacks(ContentPackManager.getInstance().getContentPacks());

        IndexManager.getInstance(project).registerProvider(this);
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
        contentPack.getData(ItemGroup.class).forEach(this::addItemGroup);
    }

    private void addItem(Item itemData) {
        ItemRef item = ItemRef.createItem(itemData.getId(), itemData.getMetadata());
        getItemDataList(item).add(itemData);
        ItemRef ignoreMetadata = ItemRef.createIgnoreMetadata(itemData.getId());
        getItemDataList(ignoreMetadata).add(itemData);
    }

    private void addOreDict(OreDict oreDict) {
        List<Item> itemData = getItemDataList(ItemRef.createOreDict(oreDict.getId()));
        oreDict.getEntries().forEach(itemRef -> itemData.addAll(getIndex(StandardIndexes.ITEMS).get(itemRef)));
    }

    private void addItemGroup(ItemGroup itemGroup) {
        getIndex(StandardIndexes.ITEM_GROUPS).put(itemGroup.getId(), itemGroup);
    }

    private List<Item> getItemDataList(ItemRef item) {
        return getIndex(StandardIndexes.ITEMS).computeIfAbsent(item, k -> new ArrayList<>(1));
    }
}
