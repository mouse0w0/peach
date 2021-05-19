package com.github.mouse0w0.peach.component;

import com.google.gson.JsonElement;

import javax.annotation.Nonnull;

public interface PersistentStateComponent {

    @Nonnull
    String getStoreFile();

    JsonElement saveState();

    void loadState(JsonElement jsonElement);

    default void noStateLoaded() {
        // Nothing to do.
    }

    default void initializeComponent() {
        // Nothing to do.
    }
}
