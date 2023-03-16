package com.github.mouse0w0.peach.service;

import com.google.gson.JsonElement;

import javax.annotation.Nonnull;

public interface PersistentService {

    @Nonnull
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
