package com.github.mouse0w0.peach.component;

import com.google.gson.JsonElement;

import javax.annotation.Nonnull;

public interface PersistentComponent {
    @Nonnull
    String getStorageFile();

    JsonElement serialize();

    void deserialize(JsonElement jsonElement);
}
