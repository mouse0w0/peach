package com.github.mouse0w0.peach.forge.element;

import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;

import java.io.IOException;
import java.nio.file.Path;

public class ElementFile<T> {
    private final Path file;
    private final ElementDefinition<T> definition;

    private boolean loaded = false;
    private T element;

    public ElementFile(Path file, ElementDefinition<T> definition) {
        this.file = file;
        this.definition = definition;
    }

    public Path getFile() {
        return file;
    }

    public ElementDefinition<T> getDefinition() {
        return definition;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public T get() {
        if (!isLoaded()) {
            throw new IllegalStateException("Element is not loaded");
        }
        return element;
    }

    public void load() {
        loaded = true;
        try {
            element = JsonUtils.readJson(file, getDefinition().getType());
        } catch (IOException e) {
            element = getDefinition().createElement();
        }
    }

    public void save() {
        if (!isLoaded()) {
            throw new IllegalStateException("Element is not loaded");
        }
        try {
            FileUtils.createFileIfNotExists(file);
            JsonUtils.writeJson(file, element);
        } catch (IOException e) {
            //TODO: show dialog
        }
    }
}
