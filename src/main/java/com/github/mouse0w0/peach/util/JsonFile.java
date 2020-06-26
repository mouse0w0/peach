package com.github.mouse0w0.peach.util;

import com.google.gson.Gson;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Supplier;

public class JsonFile<T> {
    private final Path file;
    private final Class<T> type;
    private final Gson gson;

    private final Supplier<T> defaultSupplier;

    private T value;

    public JsonFile(Path file, Class<T> type) {
        this(file, type, null);
    }

    public JsonFile(Path file, Class<T> type, Supplier<T> defaultSupplier) {
        this(file, type, defaultSupplier, JsonUtils.gson());
    }

    public JsonFile(Path file, Class<T> type, Supplier<T> defaultSupplier, Gson gson) {
        this.file = Validate.notNull(file);
        this.type = Validate.notNull(type);
        this.gson = Validate.notNull(gson);
        this.defaultSupplier = defaultSupplier;
        this.value = defaultSupplier != null ? defaultSupplier.get() : null;
    }

    public Path getFile() {
        return file;
    }

    public T get() {
        return value;
    }

    public void load() {
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                value = gson.fromJson(reader, type);
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        } else value = defaultSupplier != null ? defaultSupplier.get() : null;
    }

    public void set(T value) {
        this.value = value;
    }

    public void save() throws IOException {
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
