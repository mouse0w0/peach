package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import com.github.mouse0w0.peach.ui.control.FilePicker;
import javafx.util.StringConverter;

import java.io.File;
import java.util.Objects;

public class ResourcePicker extends FilePicker {
    private final ResourceStore resourceStore;

    public ResourcePicker(ResourceStore resourceStore) {
        this.resourceStore = resourceStore;
        setConverter(new StringConverter<>() {

            @Override
            public String toString(File object) {
                return resourceStore.relativize(object);
            }

            @Override
            public File fromString(String string) {
                return resourceStore.resolveToFile(string);
            }
        });
        fileProperty().addListener(observable -> {
            File oldFile = getFile();
            if (oldFile == null || !oldFile.isFile()) return;
            File newFile = resourceStore.store(oldFile);
            if (!Objects.equals(oldFile, newFile)) {
                setFile(newFile);
            }
        });
    }

    public ResourceStore getResourceStore() {
        return resourceStore;
    }
}
