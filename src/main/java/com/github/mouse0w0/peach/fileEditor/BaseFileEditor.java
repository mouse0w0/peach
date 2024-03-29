package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.util.Validate;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public abstract class BaseFileEditor implements FileEditor {
    private final Path file;
    private final ReadOnlyBooleanWrapper modified = new ReadOnlyBooleanWrapper(this, "modified");

    public BaseFileEditor(@NotNull Path file) {
        this.file = Validate.notNull(file);
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
