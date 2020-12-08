package com.github.mouse0w0.peach.fileEditor;

import javafx.scene.image.Image;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public abstract class BaseFileEditor implements FileEditor {
    private final Path file;

    private String name;
    private Image icon;

    public BaseFileEditor(Path file) {
        this.file = Validate.notNull(file);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    @Nonnull
    @Override
    public Path getFile() {
        return file;
    }
}
