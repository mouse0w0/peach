package com.github.mouse0w0.peach.mcmod.content;

import com.github.mouse0w0.eventbus.Listener;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.content.contentPack.ContentPack;
import com.github.mouse0w0.peach.mcmod.content.contentPack.ContentPackManager;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;
import com.github.mouse0w0.peach.mcmod.content.data.OreDictData;
import com.github.mouse0w0.peach.mcmod.element.*;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroup;
import com.github.mouse0w0.peach.mcmod.event.ElementEvent;
import com.github.mouse0w0.peach.mcmod.project.McModDescriptor;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.CachedImage;
import com.github.mouse0w0.peach.util.Disposable;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Path;
import java.util.*;

public class ContentManager implements Disposable {

    private final Project project;
    private final McModDescriptor mcModDescriptor;

    private final Path previewCache;

    private final Map<String, ContentPack> contentPackMap = new LinkedHashMap<>();

    private final List<Item> itemList = new ArrayList<>();
    private final Map<Item, List<ItemData>> itemMap = new HashMap<>();

    private final List<ItemGroupData> itemGroupList = new ArrayList<>();
    private final Map<String, ItemGroupData> itemGroupMap = new HashMap<>();

    public static ContentManager getInstance(Project project) {
        return project.getService(ContentManager.class);
    }

    public ContentManager(Project project, McModDescriptor mcModDescriptor, ElementManager elementManager) {
        this.project = project;
        this.mcModDescriptor = mcModDescriptor;

        this.previewCache = project.getPath().resolve(".peach/preview");
        FileUtils.createDirectoriesIfNotExistsSilently(previewCache);

        elementManager.getElements().forEach(file -> updateElement(elementManager.loadElement(file)));

        Peach.getEventBus().register(this);

        addContentPacks(ContentPackManager.getInstance().getContentPacks());
    }

    public void addContentPacks(Collection<ContentPack> contentPacks) {
        for (ContentPack contentPack : contentPacks) {
            addContentPack(contentPack);
        }
    }

    public void addContentPack(ContentPack contentPack) {
        if (contentPackMap.containsKey(contentPack.getId())) return;
        contentPackMap.put(contentPack.getId(), contentPack);
        contentPack.getItemData().forEach(this::addItem);
        contentPack.getOreDictionaryData().forEach(this::addOreDict);
        contentPack.getItemGroupData().forEach(this::addItemGroup);
    }

    private void addItem(ItemData itemData) {
        Item item = Item.createItem(itemData.getId(), itemData.getMetadata());
        getOrCreateItemData(item).add(itemData);
        Item ignoreMetadata = Item.createIgnoreMetadata(itemData.getId());
        getOrCreateItemData(ignoreMetadata).add(itemData);
    }

    private void addOreDict(OreDictData oreDictData) {
        List<ItemData> itemData = getOrCreateItemData(Item.createOreDict(oreDictData.getId()));
        oreDictData.getEntries().forEach(itemToken -> itemData.addAll(getItemData(itemToken)));
    }

    private void addItemGroup(ItemGroupData itemGroupData) {
        itemGroupList.add(itemGroupData);
        itemGroupMap.put(itemGroupData.getId(), itemGroupData);
    }

    public Collection<Item> getItems() {
        return itemList;
    }

    public Map<Item, List<ItemData>> getItemMap() {
        return itemMap;
    }

    private List<ItemData> getOrCreateItemData(Item item) {
        return itemMap.computeIfAbsent(item, k -> {
            itemList.add(item);
            return new ArrayList<>(1);
        });
    }

    public List<ItemData> getItemData(Item item) {
        List<ItemData> itemData = itemMap.get(item);
        return itemData != null ? itemData : Collections.emptyList();
    }

    public Collection<ItemGroupData> getItemGroups() {
        return itemGroupList;
    }

    public Map<String, ItemGroupData> getItemGroupMap() {
        return itemGroupMap;
    }

    public ItemGroupData getItemGroup(String id) {
        return itemGroupMap.get(id);
    }

    @Override
    public void dispose() {
        Peach.getEventBus().unregister(this);
    }

    private final Map<Path, Object> cachedElement = new HashMap<>();

    @Listener
    public void onUpdatedElement(ElementEvent.Updated event) {
        updateElement(event.getElement());
    }

    @Listener
    public void onDeletedElement(ElementEvent.Deleted event) {
        removeElement(event.getFile());
    }

    private void removeElement(Path file) {
        if (!cachedElement.containsKey(file)) return;
        ElementType<?> type = ElementRegistry.getInstance().getElementType(file);
        if (type == ElementTypes.ITEM) {
            Item[] items = (Item[]) cachedElement.remove(file);
            for (Item item : items) {
                itemList.remove(item);
                itemMap.remove(item);
            }
        } else if (type == ElementTypes.ITEM_GROUP) {
            ItemGroupData itemGroup = (ItemGroupData) cachedElement.remove(file);
            itemGroupList.remove(itemGroup);
            itemGroupMap.remove(itemGroup.getId());
        }
    }

    private void updateElement(Element element) {
        Path file = element.getFile();
        removeElement(file);
        if (element instanceof ItemElement) {
            ItemElement itemElement = (ItemElement) element;
            ItemData itemData = new ItemData(itemElement.getIdentifier(), 0, null, false);
            itemData.setDisplayName(itemElement.getDisplayName());

            Path previewFile = previewCache.resolve(element.getFileName() + ".png");
            ElementRegistry.getInstance().getElementType(ItemElement.class).generatePreview(project, itemElement, previewFile);
            CachedImage cachedImage = new CachedImage(previewFile, 64, 64);
            cachedImage.invalidate();
            itemData.setDisplayImage(cachedImage);

            String modId = mcModDescriptor.getModId();
            Item item = Item.createItem(modId + ":" + itemData.getId(), itemData.getMetadata());
            getOrCreateItemData(item).add(itemData);
            Item ignoreMetadata = Item.createIgnoreMetadata(modId + ":" + itemData.getId());
            getOrCreateItemData(ignoreMetadata).add(itemData);
            cachedElement.put(file, new Item[]{item, ignoreMetadata});
        } else if (element instanceof ItemGroup) {
            ItemGroup itemGroup = (ItemGroup) element;
            ItemGroupData itemGroupData = new ItemGroupData(itemGroup.getRegisterName(), null, itemGroup.getIcon());
            itemGroupData.setDisplayName(itemGroup.getDisplayName());

            itemGroupList.add(0, itemGroupData);
            itemGroupMap.put(itemGroupData.getId(), itemGroupData);
            cachedElement.put(file, itemGroupData);
        }
    }
}
