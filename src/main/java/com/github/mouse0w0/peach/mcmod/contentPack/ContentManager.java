package com.github.mouse0w0.peach.mcmod.contentPack;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.contentPack.data.ItemData;
import com.github.mouse0w0.peach.mcmod.service.McModService;
import com.github.mouse0w0.peach.util.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ContentManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentManager.class);

    private static final Path CONTENT_PACK_STORE_PATH = Paths.get(SystemUtils.USER_DIR, "content-pack");

    private final Map<String, ContentPack> contentPacks = new HashMap<>();

    private final Map<Item, List<ItemData>> itemTokenMap = new LinkedHashMap<>();

    public static ContentManager getInstance() {
        return McModService.getInstance().getContentManager();
    }

    public ContentManager() {
        try {
            FileUtils.createDirectoriesIfNotExists(CONTENT_PACK_STORE_PATH);
            Iterator<Path> iterator = Files.list(CONTENT_PACK_STORE_PATH).iterator();
            while (iterator.hasNext()) {
                Path path = iterator.next();
                try {
                    ContentPack contentPack = ContentPack.load(path);
                    contentPacks.put(contentPack.getId(), contentPack);
                    contentPack.getItemData().forEach(itemData -> {
                        getOrCreateItemData(Item.createItem(itemData.getId(), itemData.getMetadata())).add(itemData);
                        getOrCreateItemData(Item.createIgnoreMetadata(itemData.getId())).add(itemData);
                    });
                    contentPack.getOreDictionaryData().forEach(oreDictData -> {
                        List<ItemData> itemData = getOrCreateItemData(Item.createOreDict(oreDictData.getId()));
                        oreDictData.getEntries().forEach(itemToken -> itemData.addAll(getItemData(itemToken)));
                    });
                } catch (IOException e) {
                    LOGGER.warn("Cannot load content pack.", e);
                }
            }
        } catch (IOException e) {
            LOGGER.warn("Cannot load content.", e);
        }
    }

    public Collection<ContentPack> getContentPacks() {
        return contentPacks.values();
    }

    public ContentPack getContentPack(String id) {
        return contentPacks.get(id);
    }

    public Map<Item, List<ItemData>> getItemTokenMap() {
        return itemTokenMap;
    }

    private List<ItemData> getOrCreateItemData(Item item) {
        return itemTokenMap.computeIfAbsent(item, $ -> new ArrayList<>());
    }

    public List<ItemData> getItemData(Item item) {
        return itemTokenMap.getOrDefault(item, Collections.emptyList());
    }
}
