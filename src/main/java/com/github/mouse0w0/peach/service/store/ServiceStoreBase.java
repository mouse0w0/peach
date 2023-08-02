package com.github.mouse0w0.peach.service.store;

import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.service.Storage;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.util.StringUtils;
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

    public Path getStorePath() {
        return storePath;
    }

    @Override
    public void loadService(PersistentService service) {
        Class<?> serviceClass = service.getClass();
        Storage serviceStorage = serviceClass.getAnnotation(Storage.class);
        if (serviceStorage == null) {
            LOGGER.error("Not found @Storage annotation, failed to load component {}.", serviceClass);
            return;
        }

        String serviceStorageFile = serviceStorage.value();
        if (StringUtils.isEmpty(serviceStorageFile)) {
            LOGGER.error("Store file not specified, failed to load component {}.", service.getClass());
            return;
        }

        Path file = getStorePath().resolve(serviceStorageFile);
        if (Files.exists(file)) {
            try {
                service.loadState(JsonUtils.readJson(file));
            } catch (Exception e) {
                LOGGER.error("An exception has occurred, failed to load component {}", service.getClass(), e);
            }
        } else {
            service.noStateLoaded();
        }
        service.initializeService();
    }

    @Override
    public void saveService(PersistentService service) {
        Class<?> serviceClass = service.getClass();
        Storage serviceStorage = serviceClass.getAnnotation(Storage.class);
        if (serviceStorage == null) {
            LOGGER.error("Not found @Storage annotation, failed to save component {}.", serviceClass);
            return;
        }

        String serviceStorageFile = serviceStorage.value();
        if (StringUtils.isEmpty(serviceStorageFile)) {
            LOGGER.error("Storage file not specified, failed to save component {}.", serviceClass);
            return;
        }

        Path file = getStorePath().resolve(serviceStorageFile);
        try {
            JsonElement state = service.saveState();
            if (isStateNullOrEmpty(state)) {
                FileUtils.delete(file);
            } else {
                JsonUtils.writeJson(file, state);
            }
        } catch (Exception e) {
            LOGGER.error("An exception has occurred, failed to save component {}", serviceClass, e);
        }
    }

    private static boolean isStateNullOrEmpty(JsonElement state) {
        return state == null ||
                state.isJsonNull() ||
                state.isJsonArray() && state.getAsJsonArray().isEmpty() ||
                state.isJsonObject() && state.getAsJsonObject().isEmpty();
    }
}
