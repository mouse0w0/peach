package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.nio.file.Path;

public class Element<T> {
    private final Path file;
    private final ElementType<T> type;

    private T element;

    public Element(Path file, ElementType<T> type) {
        this.file = Validate.notNull(file);
        this.type = Validate.notNull(type);
        load();
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

    public T get() {
        return element;
    }

    public void load() {
        try {
            element = JsonUtils.readJson(file, getType().getType());
        } catch (IOException e) {
            element = getType().createElement();
        }
    }

    public void save() {
        try {
            JsonUtils.writeJson(file, element);
        } catch (IOException e) {
            //TODO: show dialog
        }
    }
}
