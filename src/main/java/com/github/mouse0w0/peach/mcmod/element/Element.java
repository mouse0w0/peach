package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public abstract class Element {

    private transient Path file;

    @NotNull
    public Path getFile() {
        if (file == null) {
            throw new IllegalStateException("File is not set");
        }
        return file;
    }

    public void setFile(Path file) {
        if (this.file != null) {
            throw new IllegalStateException("File cannot be set twice");
        }
        this.file = file;
    }

    @NotNull
    public String getFileName() {
        return StringUtils.substringBefore(file.getFileName().toString(), '.');
    }
}
