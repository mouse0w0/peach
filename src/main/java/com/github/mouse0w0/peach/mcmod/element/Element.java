package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.util.StringUtils;

import java.io.IOException;
import java.nio.file.Path;

public class Element<T> {
    private final Path file;
    private final ElementType<T> type;

    private boolean loaded = false;
    private T element;

    public Element(Path file, ElementType<T> type) {
        this.file = file;
        this.type = type;
    }

    public Path getFile() {
        return file;
    }

    public String getName() {
        return StringUtils.substringBefore(file.getFileName().toString(), '.');
    }

    public ElementType<T> getType() {
        return type;
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
            element = JsonUtils.readJson(file, getType().getType());
        } catch (IOException e) {
            element = getType().createElement();
        }
    }

    public void save() {
        if (!isLoaded()) {
            throw new IllegalStateException("Element is not loaded");
        }
        try {
            JsonUtils.writeJson(file, element);
        } catch (IOException e) {
            //TODO: show dialog
        }
    }
}
