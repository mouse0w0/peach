package com.github.mouse0w0.peach.mcmod.content;

import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.content.contentPack.ContentPack;
import com.github.mouse0w0.peach.mcmod.content.contentPack.ContentPackManager;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;
import com.github.mouse0w0.peach.mcmod.content.data.OreDictData;
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
        contentPack.getData(ItemData.class).forEach(this::addItem);
        contentPack.getData(OreDictData.class).forEach(this::addOreDict);
        contentPack.getData(ItemGroupData.class).forEach(this::addItemGroup);
    }

    private void addItem(ItemData itemData) {
        ItemRef item = ItemRef.createItem(itemData.getId(), itemData.getMetadata());
        getItemDataList(item).add(itemData);
        ItemRef ignoreMetadata = ItemRef.createIgnoreMetadata(itemData.getId());
        getItemDataList(ignoreMetadata).add(itemData);
    }

    private void addOreDict(OreDictData oreDictData) {
        List<ItemData> itemData = getItemDataList(ItemRef.createOreDict(oreDictData.getId()));
        oreDictData.getEntries().forEach(itemRef -> itemData.addAll(getIndex(StandardIndexes.ITEMS).get(itemRef)));
    }

    private void addItemGroup(ItemGroupData itemGroup) {
        getIndex(StandardIndexes.ITEM_GROUPS).put(itemGroup.getId(), itemGroup);
    }

    private List<ItemData> getItemDataList(ItemRef item) {
        return getIndex(StandardIndexes.ITEMS).computeIfAbsent(item, k -> new ArrayList<>(1));
    }
}
