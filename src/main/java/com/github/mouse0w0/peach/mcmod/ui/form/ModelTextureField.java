package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.javafx.control.ImagePicker;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelTextureField extends Element {
    private static final FileChooser.ExtensionFilter PNG_FILTER = new FileChooser.ExtensionFilter("PNG", "*.png");

    private final ResourceStore resourceStore;

    private final GridPane editor;

    private final Map<String, ImagePicker> textureMap = new HashMap<>();

    public ModelTextureField(ResourceStore resourceStore) {
        this.resourceStore = resourceStore;

        editor = new GridPane();
        editor.setHgap(9);
        editor.setVgap(9);
    }

    public Set<String> getTextureKeys() {
        return textureMap.keySet();
    }

    public void setTextureKeys(Set<String> keys) {
        editor.getChildren().clear();
        textureMap.clear();

        if (keys == null || keys.isEmpty()) return;

        if (keys.size() == 1) {
            String key = keys.iterator().next();
            ImagePicker imagePicker = createImagePicker();
            textureMap.put(key, imagePicker);
            editor.add(imagePicker, 0, 0);
        } else {
            int row = 0;
            for (String key : keys) {
                Text text = new Text(key);
                ImagePicker imagePicker = createImagePicker();
                textureMap.put(key, imagePicker);
                editor.addRow(row++, text, imagePicker);
            }
        }
    }

    private ImagePicker createImagePicker() {
        ImagePicker imagePicker = new ImagePicker();
        imagePicker.setFitSize(64, 64);
        imagePicker.setSmooth(false);
        imagePicker.getExtensionFilters().add(PNG_FILTER);
        imagePicker.setSelectedExtensionFilter(PNG_FILTER);
        return imagePicker;
    }

    public Map<String, String> getTextures() {
        Map<String, String> textures = new HashMap<>();
        textureMap.forEach((key, imagePicker) -> textures.put(key, resourceStore.toRelative(imagePicker.getFile())));
        return textures;
    }

    public void setTextures(Map<String, String> textures) {
        textures.forEach((key, texture) -> {
            ImagePicker imagePicker = textureMap.get(key);
            if (imagePicker != null) imagePicker.setFile(resourceStore.toAbsoluteFile(texture));
        });
    }

    @Override
    public boolean validate() {
        for (ImagePicker imagePicker : textureMap.values()) {
            File newFile = resourceStore.store(imagePicker.getFile());
            if (newFile == null) return false;
            imagePicker.setFile(newFile);
        }
        return true;
    }

    @Override
    protected Node createDefaultEditor() {
        return editor;
    }

}
