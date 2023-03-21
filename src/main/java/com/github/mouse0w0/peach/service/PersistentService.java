package com.github.mouse0w0.peach.service;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public interface PersistentService {

    @NotNull
    String getStoreFile();

    JsonElement saveState();

    void loadState(JsonElement jsonElement);

    default void noStateLoaded() {
        // Nothing to do.
    }

    default void initializeService() {
        // Nothing to do.
    }
}
