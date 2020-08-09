package com.github.mouse0w0.peach.mcmod.contentPack;

import com.github.mouse0w0.peach.mcmod.service.McModService;
import com.github.mouse0w0.peach.util.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ContentPackManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentPackManager.class);

    private static final Path CONTENT_PACK_STORE_PATH = Paths.get(SystemUtils.USER_DIR, "content-pack");

    private final Map<String, ContentPack> contentPacks = new HashMap<>();

    public static ContentPackManager getInstance() {
        return McModService.getInstance().getContentPackManager();
    }

    public ContentPackManager() {
        loadApplicationContentPacks();
    }

    private void loadApplicationContentPacks() {
        try {
            FileUtils.createDirectoriesIfNotExists(CONTENT_PACK_STORE_PATH);
            Iterator<Path> iterator = Files.list(CONTENT_PACK_STORE_PATH).iterator();
            while (iterator.hasNext()) {
                Path file = iterator.next();
                if (!Files.isRegularFile(file)) continue;
                try {
                    loadContentPack(file);
                } catch (IOException e) {
                    LOGGER.warn("Cannot load content pack. " + file, e);
                }
            }
        } catch (IOException e) {
            LOGGER.warn("Cannot load application content packs.", e);
        }
    }

    public Collection<ContentPack> getContentPacks() {
        return contentPacks.values();
    }

    public ContentPack getContentPack(String id) {
        return contentPacks.get(id);
    }

    public ContentPack loadContentPack(Path file) throws IOException {
        ContentPack contentPack = ContentPack.load(file);
        contentPacks.put(contentPack.getId(), contentPack);
        return contentPack;
    }
}
