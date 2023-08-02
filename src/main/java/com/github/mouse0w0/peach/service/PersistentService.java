package com.github.mouse0w0.peach.service;

import com.google.gson.JsonElement;

public interface PersistentService {

    JsonElement saveState();

    void loadState(JsonElement state);

    default void noStateLoaded() {
        // Nothing to do.
    }

    default void initializeService() {
        // Nothing to do.
    }
}
