package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import com.github.mouse0w0.peach.ui.control.ImagePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

import java.io.File;
import java.util.Objects;

public class TexturePicker extends ImagePicker {
    private final ResourceStore resourceStore;

    public TexturePicker(ResourceStore resourceStore) {
        this.resourceStore = resourceStore;
        fileProperty().addListener(observable -> {
            File oldFile = getFile();
            File newFile = resourceStore.store(oldFile);
            if (!Objects.equals(oldFile, newFile)) {
                setFile(newFile);
            }
        });
        resourceProperty().bindBidirectional(fileProperty(), new StringConverter<>() {
            @Override
            public String toString(File object) {
                return resourceStore.relativize(object);
            }

            @Override
            public File fromString(String string) {
                return resourceStore.resolveToFile(string);
            }
        });
    }

    public final ResourceStore getResourceStore() {
        return resourceStore;
    }

    private final StringProperty resource = new SimpleStringProperty(this, "resource");

    public final StringProperty resourceProperty() {
        return resource;
    }

    public final String getResource() {
        return resource.get();
    }

    public final void setResource(String value) {
        this.resource.set(value);
    }
}
