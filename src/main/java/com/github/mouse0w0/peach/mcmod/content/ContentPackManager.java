package com.github.mouse0w0.peach.mcmod.content;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.util.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContentPackManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentPackManager.class);

    private static final Path CONTENT_PACK_STORE_PATH = Paths.get(SystemUtils.USER_DIR, "content-pack");

    private final Map<String, ContentPack> contentPacks = new HashMap<>();

    public static ContentPackManager getInstance() {
        return Peach.getInstance().getService(ContentPackManager.class);
    }

    public ContentPackManager() {
        loadApplicationContentPacks();
    }

    private void loadApplicationContentPacks() {
        try {
            FileUtils.createDirectoriesIfNotExists(CONTENT_PACK_STORE_PATH);
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(CONTENT_PACK_STORE_PATH, Files::isRegularFile)) {
                for (Path file : stream) {
                    try {
                        ContentPack contentPack = loadContentPack(file);
                        LOGGER.info("Loaded content pack: {}", contentPack.getId());
                    } catch (IOException e) {
                        LOGGER.warn("Cannot load content pack, path={}", file, e);
                    }
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
