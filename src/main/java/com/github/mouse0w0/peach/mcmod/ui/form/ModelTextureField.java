package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.peach.form.field.Field;
import com.github.mouse0w0.peach.javafx.binding.BidirectionalValueBinding;
import com.github.mouse0w0.peach.javafx.control.ImagePicker;
import com.github.mouse0w0.peach.javafx.util.ExtensionFilters;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.io.File;
import java.util.*;

public class ModelTextureField extends Field {
    private final ResourceStore resourceStore;

    private final GridPane editor;

    private final Set<String> textureKeys = new LinkedHashSet<>();
    private final ObservableMap<String, File> textures = FXCollections.observableHashMap();

    public ModelTextureField(ResourceStore resourceStore) {
        this.resourceStore = resourceStore;

        editor = new GridPane();
        editor.setMaxWidth(Region.USE_PREF_SIZE);
        editor.setAlignment(Pos.CENTER);
        editor.setHgap(8);
        editor.setVgap(4);
    }

    public Set<String> getTextureKeys() {
        return textureKeys;
    }

    public void setTextureKeys(Collection<String> keys) {
        textureKeys.clear();

        if (keys == null) return;

        textureKeys.addAll(keys);
        editor.getChildren().clear();
        if (textureKeys.size() == 1) {
            String textureKey = textureKeys.iterator().next();
            editor.add(createImagePicker(textureKey), 0, 0);
        } else {
            int column = 0;
            for (String textureKey : textureKeys) {
                editor.add(createImagePicker(textureKey), column, 0);
                editor.add(new Text(textureKey), column, 1);
                column++;
            }
        }
    }

    private ImagePicker createImagePicker(String textureKey) {
        ImagePicker imagePicker = new ImagePicker();
        imagePicker.setFitSize(64, 64);
        imagePicker.setSmooth(false);
        imagePicker.getExtensionFilters().add(ExtensionFilters.PNG);
        imagePicker.fileProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(oldValue, newValue)) return;
            imagePicker.setFile(resourceStore.store(newValue));
        });
        BidirectionalValueBinding.bind(imagePicker.fileProperty(), textures, textureKey);
        return imagePicker;
    }

    public Map<String, String> getTextures() {
        Map<String, String> result = new HashMap<>();
        textureKeys.forEach(k -> result.put(k, resourceStore.toRelative(textures.get(k))));
        return result;
    }

    public void setTextures(Map<String, String> map) {
        map.forEach((k, v) -> textures.put(k, resourceStore.toAbsoluteFile(v)));
    }

    @Override
    protected Node createEditorNode() {
        return editor;
    }

}
