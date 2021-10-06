package com.github.mouse0w0.peach.fileEditor;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.image.Image;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public abstract class BaseFileEditor implements FileEditor {
    private final Path file;

    private final ReadOnlyBooleanWrapper modified;

    private String name;
    private Image icon;

    public BaseFileEditor(@Nonnull Path file) {
        this.file = Validate.notNull(file);
        this.modified = new ReadOnlyBooleanWrapper(this, "modified");
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

    @Override
    public ReadOnlyBooleanProperty modifiedProperty() {
        return modified.getReadOnlyProperty();
    }

    @Override
    public boolean isModified() {
        return modified.get();
    }

    protected void setModified(boolean modified) {
        this.modified.set(modified);
    }
}
