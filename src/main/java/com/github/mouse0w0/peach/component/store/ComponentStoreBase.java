package com.github.mouse0w0.peach.component.store;

import com.github.mouse0w0.peach.component.PersistentStateComponent;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ComponentStoreBase implements ComponentStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppComponentStore.class);

    private final Path storePath;

    public ComponentStoreBase(Path storePath) {
        this.storePath = storePath;
        FileUtils.createDirectoriesIfNotExists(storePath);
    }

    @Override
    public Path getStorePath() {
        return storePath;
    }

    @Override
    public void loadComponent(PersistentStateComponent component) {
        String storeFile = component.getStoreFile();

        if (storeFile == null || storeFile.isEmpty()) {
            LOGGER.error("Store file not specified, failed to load component {}.", component.getClass());
            return;
        }

        Path file = getStorePath().resolve(storeFile);
        if (Files.exists(file)) {
            try {
                component.loadState(JsonUtils.readJson(file, JsonElement.class));
            } catch (Exception e) {
                LOGGER.error("An exception has occurred, failed to load component " + component.getClass() + ".", e);
            }
        } else {
            component.noStateLoaded();
        }
        component.initializeComponent();
    }

    @Override
    public void saveComponent(PersistentStateComponent component) {
        String storageFile = component.getStoreFile();

        if (storageFile == null || storageFile.isEmpty()) {
            LOGGER.error("Storage file not specified, failed to save component {}.", component.getClass());
            return;
        }

        Path file = getStorePath().resolve(storageFile);
        try {
            JsonUtils.writeJson(file, component.saveState());
        } catch (Exception e) {
            LOGGER.error("An exception has occurred, failed to save component " + component.getClass() + ".", e);
        }
    }
}
