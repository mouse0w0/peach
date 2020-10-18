package com.github.mouse0w0.peach.util;

import com.github.mouse0w0.peach.exception.RuntimeIOException;
import com.google.gson.Gson;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class JsonFile<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFile.class);

    private final Path file;
    private final Class<T> type;
    private final Gson gson;

    private boolean loaded;
    private T value;

    public JsonFile(Path file, Class<T> type) {
        this(file, type, JsonUtils.gson());
    }

    public JsonFile(Path file, Class<T> type, Gson gson) {
        this.file = Validate.notNull(file);
        this.type = Validate.notNull(type);
        this.gson = Validate.notNull(gson);
    }

    public Path getFile() {
        return file;
    }

    public boolean exists() {
        return Files.exists(getFile());
    }

    public T get() {
        if (!loaded) throw new IllegalStateException("File not loaded");
        return value;
    }

    public JsonFile<T> load() {
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                value = gson.fromJson(reader, type);
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        }

        if (value == null) {
            try {
                value = type.newInstance();
            } catch (ReflectiveOperationException e) {
                LOGGER.error("Cannot new instance for " + type.getName(), e);
            }
        }

        loaded = true;
        return this;
    }

    public void set(T value) {
        this.value = value;
    }

    public void save() {
        try {
            FileUtils.createFileIfNotExists(file);
            try (Writer writer = Files.newBufferedWriter(file)) {
                JsonUtils.gson().toJson(value, writer);
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    @Override
    public String toString() {
        return "JsonFile{" +
                "file=" + file +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonFile<?> jsonFile = (JsonFile<?>) o;
        return file.equals(jsonFile.file) &&
                type.equals(jsonFile.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, type);
    }
}
