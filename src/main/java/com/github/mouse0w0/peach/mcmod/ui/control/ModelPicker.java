package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.model.BlockstateTemplate;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.model.ModelTemplate;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.binding.BidirectionalValueBinding;
import com.github.mouse0w0.peach.ui.layout.FormItem;
import com.github.mouse0w0.peach.ui.util.ExtensionFilters;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonObject;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.*;

import static com.github.mouse0w0.peach.ui.layout.FormItem.one;

public class ModelPicker extends VBox {
    private final ModelManager modelManager;
    private final ResourceStore modelStore;
    private final ResourceStore textureStore;

    private final ComboBox<Identifier> modelField = new ComboBox<>();
    private final VBox customModelsPane = new VBox();
    private final FormItem customModelsItem;
    private final GridPane texturesPane = new GridPane();
    private final FormItem texturesItem;

    public ModelPicker(Project project,
                       String modelText,
                       ResourceStore modelStore,
                       String textureText,
                       ResourceStore textureStore) {
        this.modelManager = ModelManager.getInstance(project);
        this.modelStore = modelStore;
        this.textureStore = textureStore;

        getStyleClass().add("model-picker");

        modelField.getStyleClass().add("model");
        customModelsPane.getStyleClass().add("custom-models-pane");
        texturesPane.getStyleClass().add("textures-pane");

        modelField.valueProperty().bindBidirectional(modelProperty());
        modelField.setConverter(new StringConverter<>() {
            @Override
            public String toString(Identifier id) {
                if (id == null) return null;
                ModelTemplate modelTemplate = modelManager.getModelTemplate(id);
                if (modelTemplate != null) return modelTemplate.getLocalizedName();
                return AppL10n.localize("model." + id.getNamespace() + "." + id.getPath());
            }

            @Override
            public Identifier fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });

        getChildren().addAll(
                one(modelText, modelField),
                customModelsItem = one(customModelsPane),
                texturesItem = one(textureText, texturesPane)
        );

        defaultModelEnabledProperty().addListener(observable -> updateModelField());
        blockstateProperty().addListener(observable -> updateModelField());
        modelProperty().addListener(observable -> {
            updateCustomModels();
            updateTextures();
        });
        customModels.addListener((InvalidationListener) observable -> updateTextures());
        updateModelField();
    }

    private final StringProperty blockstate = new SimpleStringProperty(this, "blockstate");

    public final StringProperty blockstateProperty() {
        return blockstate;
    }

    public final String getBlockstate() {
        return blockstate.get();
    }

    public final void setBlockstate(String value) {
        blockstate.set(value);
    }

    private final ObjectProperty<Identifier> model = new SimpleObjectProperty<>(this, "model");

    public final ObjectProperty<Identifier> modelProperty() {
        return model;
    }

    public final Identifier getModel() {
        return model.get();
    }

    public final void setModel(Identifier value) {
        model.set(value);
    }

    private final BooleanProperty defaultModelEnabled = new SimpleBooleanProperty(this, "defaultModelEnabled");

    public final BooleanProperty defaultModelEnabledProperty() {
        return defaultModelEnabled;
    }

    public final boolean isDefaultModelEnabled() {
        return defaultModelEnabled.get();
    }

    public final void setDefaultModelEnabled(boolean value) {
        defaultModelEnabled.set(value);
    }

    private final ObservableMap<String, String> customModels = FXCollections.observableHashMap();

    public Map<String, String> getCustomModels() {
        if (ModelManager.CUSTOM.equals(getModel())) {
            Map<String, String> result = new HashMap<>();
            for (String modelKey : blockstateTemplateCache.getModels().keySet()) {
                result.put(modelKey, customModels.get(modelKey));
            }
            return result;
        } else {
            return Collections.emptyMap();
        }
    }

    public final void setCustomModels(Map<String, String> customModels) {
        try {
            updatingTextures = true;
            this.customModels.putAll(customModels);
        } finally {
            updatingTextures = false;
        }
        updateTextures();
    }

    private BlockstateTemplate blockstateTemplateCache;

    private void updateModelField() {
        List<Identifier> modelItems = modelField.getItems();
        modelItems.clear();

        customModelsPane.getChildren().clear();
        customModelsPaneInitialized = false;

        String blockstate = getBlockstate();

        blockstateTemplateCache = modelManager.getBlockstateTemplate(blockstate);
        if (blockstateTemplateCache == null) return;

        if (isDefaultModelEnabled()) {
            modelItems.add(ModelManager.DEFAULT);
        }
        modelItems.addAll(modelManager.getModelTemplatesByBlockstate(blockstate));
        modelItems.add(ModelManager.CUSTOM);

        Identifier model = getModel();
        if (modelItems.contains(model)) {
            modelField.getSelectionModel().select(model);
        } else {
            modelField.getSelectionModel().selectFirst();
        }

        updateCustomModels();
    }

    private void updateCustomModels() {
        boolean isCustomModel = ModelManager.CUSTOM.equals(getModel());
        customModelsItem.setVisible(isCustomModel);
        customModelsItem.setManaged(isCustomModel);
        if (isCustomModel) {
            initCustomModelsPane();
        }
    }

    private boolean customModelsPaneInitialized = false;

    private void initCustomModelsPane() {
        if (customModelsPaneInitialized) return;
        customModelsPaneInitialized = true;

        if (blockstateTemplateCache == null) return;

        ObservableList<Node> children = customModelsPane.getChildren();
        for (String modelKey : blockstateTemplateCache.getModels().keySet()) {
            ResourcePicker modelPicker = new ResourcePicker(modelStore);
            modelPicker.getExtensionFilters().add(ExtensionFilters.JSON);
            BidirectionalValueBinding.bind(modelPicker.valueProperty(), customModels, modelKey);
            children.add(one(modelKey, modelPicker));
        }
    }

    private final Set<String> textureKeys = new LinkedHashSet<>();
    private final Map<String, Set<String>> modelTextureKeysCache = new HashMap<>();
    private final ObservableMap<String, String> textures = FXCollections.observableHashMap();
    private boolean updatingTextures = false;

    public final Map<String, String> getTextures() {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : textures.entrySet()) {
            if (textureKeys.contains(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public final void setTextures(Map<String, String> textures) {
        this.textures.putAll(textures);
    }

    private void updateTextures() {
        if (updatingTextures) return;

        Identifier model = getModel();
        if (model == null) return;

        textureKeys.clear();
        texturesPane.getChildren().clear();

        if (ModelManager.DEFAULT.equals(model)) {
            texturesItem.setVisible(false);
            texturesItem.setManaged(false);
            return;
        }

        texturesItem.setVisible(true);
        texturesItem.setManaged(true);
        if (ModelManager.CUSTOM.equals(model)) {
            if (blockstateTemplateCache != null) {
                for (String modelKey : blockstateTemplateCache.getModels().keySet()) {
                    String modelPath = customModels.get(modelKey);
                    if (modelPath == null) continue;
                    Set<String> textureKeys = getTextureKeysFromModel(modelPath);
                    this.textureKeys.addAll(textureKeys);
                }
            }
        } else {
            textureKeys.addAll(modelManager.getModelTemplate(model).getTextures());
        }

        rebuildTexturesPane();
    }

    private Set<String> getTextureKeysFromModel(String modelPath) {
        return modelTextureKeysCache.computeIfAbsent(modelPath, this::loadTextureKeysFromModel);
    }

    private Set<String> loadTextureKeysFromModel(String modelPath) {
        try {
            JsonObject root = JsonUtils.readJson(modelStore.resolve(modelPath)).getAsJsonObject();
            JsonObject textures = root.get("textures").getAsJsonObject();
            return new LinkedHashSet<>(textures.keySet());
        } catch (IOException | IllegalStateException ignored) {
            // TODO: alert
        }
        return Collections.emptySet();
    }

    private void rebuildTexturesPane() {
        if (textureKeys.size() == 1) {
            texturesPane.add(createTexturePicker(textureKeys.iterator().next()), 0, 0);
        } else {
            int column = 0;
            for (String textureKey : textureKeys) {
                texturesPane.add(createTexturePicker(textureKey), column, 0);
                texturesPane.add(new Label(textureKey), column, 1);
                column++;
            }
        }
    }

    private TexturePicker createTexturePicker(String textureKey) {
        TexturePicker texturePicker = new TexturePicker(textureStore);
        texturePicker.setFitSize(64, 64);
        texturePicker.setSmooth(false);
        texturePicker.getExtensionFilters().add(ExtensionFilters.PNG);
        BidirectionalValueBinding.bind(texturePicker.resourceProperty(), textures, textureKey);
        return texturePicker;
    }

    @Override
    public String getUserAgentStylesheet() {
        return ModelPicker.class.getResource("ModelPicker.css").toExternalForm();
    }
}
