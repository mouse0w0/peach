package com.github.mouse0w0.peach.forge.contentPack;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.forge.contentPack.data.ItemData;
import com.github.mouse0w0.peach.forge.contentPack.data.OreDictData;
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

    private final List<ItemData> itemDataList = new ArrayList<>();
    private final Map<String, OreDictData> oreDictDataMap = new HashMap<>();

    public static ContentManager getInstance() {
        return Peach.getInstance().getService(ContentManager.class);
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
                    itemDataList.addAll(contentPack.getItemData());
                    contentPack.getOreDictionaryData().forEach(oreDictData -> oreDictDataMap.put(oreDictData.getId(), oreDictData));
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

    public List<ItemData> getItemDataList() {
        return itemDataList;
    }

    public Map<String, OreDictData> getOreDictDataMap() {
        return oreDictDataMap;
    }
}
