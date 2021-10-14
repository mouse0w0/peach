package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.javafx.control.ImagePicker;
import com.github.mouse0w0.peach.mcmod.model.TextureEntry;
import com.github.mouse0w0.peach.mcmod.model.TextureList;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import javafx.geometry.HPos;
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
        editor.setHgap(8);
        editor.setVgap(4);
    }

    public Set<String> getTextureKeys() {
        return textureMap.keySet();
    }

    public void setTextureList(TextureList textureList) {
        editor.getChildren().clear();
        textureMap.clear();

        if (textureList == null) return;

        if (textureList.isCustomLayout()) {
            for (TextureEntry texture : textureList) {
                ImagePicker imagePicker = createImagePicker(texture);
                Text text = createText(texture);
                editor.add(imagePicker, texture.getColumn(), texture.getRow() * 2);
                editor.add(text, texture.getColumn(), texture.getRow() * 2 + 1);
            }
            return;
        }

        if (textureList.size() == 1) {
            TextureEntry texture = textureList.get(0);
            ImagePicker imagePicker = createImagePicker(texture);
            editor.add(imagePicker, 0, 0);
            return;
        }

        int column = 0;
        for (TextureEntry texture : textureList) {
            ImagePicker imagePicker = createImagePicker(texture);
            Text text = createText(texture);
            editor.addColumn(column++, imagePicker, text);
        }
    }

    private ImagePicker createImagePicker(TextureEntry texture) {
        ImagePicker imagePicker = new ImagePicker();
        imagePicker.setFitSize(64, 64);
        imagePicker.setSmooth(false);
        imagePicker.getExtensionFilters().add(PNG_FILTER);
        imagePicker.setSelectedExtensionFilter(PNG_FILTER);
        textureMap.put(texture.getKey(), imagePicker);
        return imagePicker;
    }

    private Text createText(TextureEntry texture) {
        Text text;
        if (texture.getTranslationKey() != null) {
            text = new Text(I18n.translate(texture.getTranslationKey(), texture.getKey()));
        } else {
            text = new Text(texture.getKey());
        }
        GridPane.setHalignment(text, HPos.CENTER);
        return text;
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
