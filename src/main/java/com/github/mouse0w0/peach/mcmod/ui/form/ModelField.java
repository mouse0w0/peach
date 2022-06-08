package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.fileWatch.FileChangeListener;
import com.github.mouse0w0.peach.fileWatch.ProjectFileWatcher;
import com.github.mouse0w0.peach.fileWatch.WeakFileChangeListener;
import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.javafx.binding.BidirectionalValueBinding;
import com.github.mouse0w0.peach.javafx.control.FilePicker;
import com.github.mouse0w0.peach.javafx.util.ExtensionFilters;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.util.Scheduler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ModelField extends Element {

    private final Project project;
    private final ModelManager modelManager;
    private final ResourceStore resourceStore;

    public ModelField(Project project, ResourceStore resourceStore) {
        this.project = project;
        this.modelManager = ModelManager.getInstance();
        this.resourceStore = resourceStore;

        modelPrototypeProperty().addListener(observable -> updateTexture());
        models.addListener((InvalidationListener) observable -> updateTexture());

        ProjectFileWatcher.getInstance(project).addListener(new WeakFileChangeListener(fileChangeListener));
    }

    private StringProperty text;

    public final StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(this, "text");
        }
        return text;
    }

    public final String getText() {
        return text != null ? text.get() : null;
    }

    public final void setText(String text) {
        textProperty().set(text);
    }

    private final StringProperty blockstate = new SimpleStringProperty(this, "blockstate");

    public final StringProperty blockstateProperty() {
        return blockstate;
    }

    public final String getBlockstate() {
        return blockstate.get();
    }

    public final void setBlockstate(String blockstate) {
        blockstateProperty().set(blockstate);
    }

    private BooleanProperty inherit;

    public final BooleanProperty inheritProperty() {
        if (inherit == null) {
            inherit = new SimpleBooleanProperty(this, "inherit");
        }
        return inherit;
    }

    public final boolean isInherit() {
        return inherit != null && inherit.get();
    }

    public final void setInherit(boolean inherit) {
        inheritProperty().set(inherit);
    }

    private final ObjectProperty<Identifier> modelPrototype = new SimpleObjectProperty<>(this, "model");

    public final ObjectProperty<Identifier> modelPrototypeProperty() {
        return modelPrototype;
    }

    public final Identifier getModelPrototype() {
        return modelPrototype.get();
    }

    public final void setModelPrototype(Identifier modelPrototype) {
        modelPrototypeProperty().set(modelPrototype);
    }

    private final ObservableMap<String, String> models = FXCollections.observableHashMap();

    public final Map<String, String> getModels() {
        if (ModelManager.CUSTOM.equals(getModelPrototype())) {
            final Map<String, String> result = new HashMap<>();
            for (String modelKey : modelManager.getBlockstate(getBlockstate()).getModels()) {
                result.put(modelKey, models.get(modelKey));
            }
            return result;
        } else {
            return Collections.emptyMap();
        }
    }

    public final void setModels(Map<String, String> map) {
        try {
            updating = true;
            models.putAll(map);
        } finally {
            updating = false;
        }
        updateTexture();
    }

    private final ObservableList<String> textures = FXCollections.observableArrayList();
    private final ObservableList<String> unmodifiableTextures = FXCollections.unmodifiableObservableList(textures);

    public final ObservableList<String> getTextures() {
        return unmodifiableTextures;
    }

    private GridPane pane;
    private Label label;
    private ComboBox<Identifier> comboBox;

    @Override
    protected Node createDefaultNode() {
        pane = new GridPane();
        pane.setVgap(9);
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.visibleProperty().bind(visibleProperty());
        pane.managedProperty().bind(visibleProperty());
        pane.idProperty().bind(idProperty());
        Bindings.bindContent(pane.getStyleClass(), getStyleClass());
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPercentWidth(16.67);
        pane.getColumnConstraints().add(labelColumn);
        ColumnConstraints editorColumn = new ColumnConstraints();
        editorColumn.setPercentWidth(83.33);
        pane.getColumnConstraints().add(editorColumn);

        label = new Label();
        label.getStyleClass().setAll("label", "form-item-label");
        label.setWrapText(true);
        label.textProperty().bind(textProperty());
        pane.add(label, 0, 0);

        comboBox = new ComboBox<>();
        comboBox.setPrefWidth(10000);
        comboBox.valueProperty().bindBidirectional(modelPrototypeProperty());
        comboBox.disableProperty().bind(disableProperty());
        comboBox.setConverter(new StringConverter<Identifier>() {
            @Override
            public String toString(Identifier object) {
                return I18n.translate("model." + object.getNamespace() + "." + object.getName());
            }

            @Override
            public Identifier fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        pane.add(comboBox, 1, 0);

        modelPrototypeProperty().addListener(observable -> updateModelPrototype());
        blockstateProperty().addListener(observable -> updateBlockstate());
        inheritProperty().addListener(observable -> updateBlockstate());
        updateBlockstate();

        return pane;
    }

    private void updateBlockstate() {
        final String blockstate = getBlockstate();
        final Identifier modelPrototype = getModelPrototype();
        final List<Identifier> modelList = new ArrayList<>();
        if (blockstate != null) {
            if (isInherit()) {
                modelList.add(ModelManager.INHERIT);
            }
            modelList.addAll(modelManager.getModelPrototypes(blockstate));
            modelList.add(ModelManager.CUSTOM);
        }
        comboBox.getItems().setAll(modelList);
        if (modelList.contains(modelPrototype)) {
            comboBox.getSelectionModel().select(modelPrototype);
        } else {
            comboBox.getSelectionModel().selectFirst();
        }
        updateModelPrototype();
    }

    private void updateTexture() {
        if (updating) return;
        final Identifier modelPrototype = getModelPrototype();
        if (ModelManager.INHERIT.equals(modelPrototype)) {
            textures.clear();
        } else if (ModelManager.CUSTOM.equals(modelPrototype)) {
            textures.clear();
            fileToModelKey.clear();
            for (String modelKey : modelManager.getBlockstate(getBlockstate()).getModels()) {
                fileToModelKey.put(resourceStore.toAbsolutePath(models.get(modelKey)), modelKey);
            }
            loadTexture();
        } else {
            textures.setAll(modelManager.getModelPrototype(modelPrototype).getTextures());
        }
    }

    private void updateModelPrototype() {
        ObservableList<Node> children = pane.getChildren();
        if (ModelManager.CUSTOM.equals(getModelPrototype())) {
            children.removeIf(node -> node != label && node != comboBox);

            int row = 1;
            for (String modelKey : modelManager.getBlockstate(getBlockstate()).getModels()) {
                Label label = new Label(modelKey);
                label.getStyleClass().setAll("label", "form-item-label");
                label.setWrapText(true);
                pane.add(label, 0, row);

                FilePicker filePicker = new FilePicker();
                filePicker.getExtensionFilters().add(ExtensionFilters.JSON);
                filePicker.setConverter(new StringConverter<File>() {
                    @Override
                    public String toString(File object) {
                        File result = resourceStore.store(object);
                        return result != null ? resourceStore.toRelative(result) : filePicker.getValue();
                    }

                    @Override
                    public File fromString(String string) {
                        return resourceStore.toAbsoluteFile(string);
                    }
                });
                BidirectionalValueBinding.bind(filePicker.valueProperty(), models, modelKey);
                pane.add(filePicker, 1, row);
                row++;
            }
        } else {
            if (children.size() > 2) children.removeIf(node -> node != label && node != comboBox);
        }
    }

    private boolean updating;

    private final FileChangeListener fileChangeListener = new FileChangeListener() {
        @Override
        public void onFileDelete(ProjectFileWatcher watcher, Path path) {
            if (fileToModelKey.containsKey(path)) {
                Platform.runLater(() -> {
                    try {
                        updating = true;
                        for (String modelKey : fileToModelKey.get(path)) {
                            models.remove(modelKey);
                        }
                    } finally {
                        updating = false;
                    }
                    updateTexture();
                });
            }
        }

        @Override
        public void onFileModify(ProjectFileWatcher watcher, Path path) {
            if (fileToModelKey.containsKey(path)) {
                Platform.runLater(() -> loadTexture());
            }
        }
    };
    private final Multimap<Path, String> fileToModelKey = HashMultimap.create();

    private Future<?> loadTextureFuture;

    private void loadTexture() {
        if (loadTextureFuture != null) {
            loadTextureFuture.cancel(true);
        }
        loadTextureFuture = Scheduler.getInstance().schedule(() -> {
            final Set<String> result = loadTexture0(fileToModelKey.keySet());
            Platform.runLater(() -> textures.setAll(result));
        }, 100, TimeUnit.MILLISECONDS);
    }

    private Set<String> loadTexture0(final Set<Path> modelFiles) {
        final Set<String> result = new LinkedHashSet<>();
        for (Path modelFile : modelFiles) {
            try {
                JsonObject root = JsonUtils.readJson(modelFile).getAsJsonObject();
                JsonObject textures = root.get("textures").getAsJsonObject();
                result.addAll(textures.keySet());
            } catch (IOException | IllegalStateException ignored) {
                // TODO: alert
            }
        }
        return result;
    }
}
