package com.github.mouse0w0.peach.service.store;

import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ServiceStoreBase implements ServiceStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceStoreBase.class);

    private final Path storePath;

    public ServiceStoreBase(Path storePath) {
        this.storePath = storePath;
        FileUtils.createDirectoriesIfNotExists(storePath);
    }

    @Override
    public Path getStorePath() {
        return storePath;
    }

    @Override
    public void loadService(PersistentService service) {
        String storeFile = service.getStoreFile();

        if (storeFile == null || storeFile.isEmpty()) {
            LOGGER.error("Store file not specified, failed to load component {}.", service.getClass());
            return;
        }

        Path file = getStorePath().resolve(storeFile);
        if (Files.exists(file)) {
            try {
                service.loadState(JsonUtils.readJson(file, JsonElement.class));
            } catch (Exception e) {
                LOGGER.error("An exception has occurred, failed to load component " + service.getClass() + ".", e);
            }
        } else {
            service.noStateLoaded();
        }
        service.initializeService();
    }

    @Override
    public void saveService(PersistentService service) {
        String storageFile = service.getStoreFile();

        if (storageFile == null || storageFile.isEmpty()) {
            LOGGER.error("Storage file not specified, failed to save component {}.", service.getClass());
            return;
        }

        Path file = getStorePath().resolve(storageFile);
        try {
            JsonUtils.writeJson(file, service.saveState());
        } catch (Exception e) {
            LOGGER.error("An exception has occurred, failed to save component " + service.getClass() + ".", e);
        }
    }
}
