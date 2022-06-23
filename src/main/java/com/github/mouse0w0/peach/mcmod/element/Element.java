package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.util.StringUtils;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public abstract class Element {

    private transient Path file;

    public static void setFile(Element element, Path file) {
        if (element.file != null) {
            throw new IllegalStateException("The file of element has been initialized");
        }
        element.file = file;
    }

    @Nonnull
    public Path getFile() {
        if (file == null) {
            throw new IllegalStateException("The file of element not initialized");
        }
        return file;
    }

    @Nonnull
    public String getFileName() {
        return StringUtils.substringBefore(file.getFileName().toString(), '.');
    }
}
