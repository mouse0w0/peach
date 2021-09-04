package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.javafx.control.ImagePicker;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

import java.io.File;

public class TextureField extends Element {
    private final ObjectProperty<File> value = new SimpleObjectProperty<>(this, "value");

    private final ResourceStore resourceStore;

    public TextureField(ResourceStore resourceStore) {
        this.resourceStore = resourceStore;
    }

    public ObjectProperty<File> valueProperty() {
        return value;
    }

    public File getValue() {
        return value.get();
    }

    public void setValue(File value) {
        valueProperty().setValue(value);
    }

    public String getTexture() {
        return resourceStore.toRelative(getValue());
    }

    public void setTexture(String texture) {
        setValue(resourceStore.toAbsoluteFile(texture));
    }

    public void setFitSize(double fitWidth, double fitHeight) {
        getImagePicker().setFitSize(fitWidth, fitHeight);
    }

    public ImagePicker getImagePicker() {
        return (ImagePicker) getEditor();
    }

    @Override
    public boolean validate() {
        File file = getValue();
        if (file == null) {
            return true;
        }

        File newFile = resourceStore.store(file);
        if (newFile != null) {
            setValue(newFile);
            return true;
        }
        return false;
    }

    @Override
    protected Node createDefaultEditor() {
        ImagePicker imagePicker = new ImagePicker();
        imagePicker.fileProperty().bindBidirectional(valueProperty());
        imagePicker.maxWidthProperty().bind(imagePicker.fitWidthProperty());
        imagePicker.maxHeightProperty().bind(imagePicker.fitHeightProperty());
        imagePicker.disableProperty().bind(disableProperty());
        return imagePicker;
    }
}
