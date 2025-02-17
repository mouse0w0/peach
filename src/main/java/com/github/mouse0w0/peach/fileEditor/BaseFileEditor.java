package com.github.mouse0w0.peach.fileEditor;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

public abstract class BaseFileEditor implements FileEditor {
    private final Path file;
    private final ReadOnlyBooleanWrapper modified = new ReadOnlyBooleanWrapper(this, "modified");

    public BaseFileEditor(@NotNull Path file) {
        this.file = Objects.requireNonNull(file);
    }

    @NotNull
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

    @Override
    public void dispose() {
        // Nothing to do
    }
}
