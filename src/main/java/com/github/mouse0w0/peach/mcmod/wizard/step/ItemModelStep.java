package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ItemElement;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.model.json.JsonModel;
import com.github.mouse0w0.peach.mcmod.project.McModDataKeys;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.CachedImage;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.wizard.WizardStep;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ItemModelStep extends FlowPane implements WizardStep {

    private final Element<ItemElement> element;
    private final Project project;

    @FXML
    private ChoiceBox<String> model;
    @FXML
    private GridPane content;

    private Map<String, String> textures = new LinkedHashMap<>();
    private Map<String, ImageView> imageViewMap = new HashMap<>();

    public ItemModelStep(Element<ItemElement> element) {
        this.element = element;
        this.project = WindowManager.getInstance().getFocusedWindow().getProject();

        FXUtils.loadFXML(this, "ui/mcmod/ItemModel.fxml");

        model.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return I18n.translate("model.item." + object, object);
            }

            @Override
            public String fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        ModelManager modelManager = ModelManager.getInstance(project);
        model.getItems().addAll(ModelManager.getInstance(project).getItemModels().keySet());
        model.valueProperty().addListener(observable -> {
            clearTextures();

            JsonModel model = modelManager.getItemModel(this.model.getValue());
            if (model == null) return;

            Set<String> textures = model.getTextures().keySet();
            int row = 2;
            for (String texture : textures) {
                content.add(new Text(texture), 0, row);

                BorderPane pane = new BorderPane();
                FXUtils.setFixedSize(pane, 64, 64);
                pane.getStyleClass().add("texture");
                content.add(pane, 1, row);

                ImageView imageView = new ImageView();
                imageView.setFitWidth(64);
                imageView.setFitHeight(64);
                imageViewMap.put(texture, imageView);
                pane.setCenter(imageView);

                pane.setOnDragOver(event -> {
                    event.consume();
                    if (event.getGestureSource() == event.getTarget()) return;

                    List<File> files = event.getDragboard().getFiles();
                    if (files == null || !files.get(0).getName().endsWith(".png")) return;

                    event.acceptTransferModes(TransferMode.COPY);
                });
                pane.setOnDragDropped(event -> {
                    event.consume();
                    try {
                        Path source = event.getDragboard().getFiles().get(0).toPath();
                        String textureName = "items/" + FileUtils.getFileNameWithoutExtensionName(source);
                        Path target = getItemTextureFilePath(textureName);
                        FileUtils.copyIfNotExists(source, target);
                        setTexture(texture, textureName);
                        event.setDropCompleted(true);
                    } catch (IOException ignored) {
                        ignored.printStackTrace();
                    }
                });

                row++;
            }
        });
    }

    private Path getItemTextureFilePath(String textureName) {
        return project.getData(McModDataKeys.RESOURCES_PATH).resolve("textures/" + textureName + ".png");
    }

    private void clearTextures() {
        ObservableList<Node> children = content.getChildren();
        if (children.size() > 3) children.remove(3, children.size());

        imageViewMap.clear();
        textures.clear();
    }

    private void setTexture(String texture, String fileName) {
        imageViewMap.get(texture).setImage(new CachedImage(getItemTextureFilePath(fileName), 64, 64).getImage());
        textures.put(texture, fileName);
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public void init() {
        ItemElement item = element.get();

        model.getSelectionModel().select(item.getModel());
        if (model.getSelectionModel().isEmpty()) model.getSelectionModel().select("generated");

        item.getTextures().forEach(this::setTexture);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void updateDataModel() {
        ItemElement item = element.get();
        item.setModel(model.getValue());
        item.setTextures(textures);
    }

    @Override
    public void dispose() {
        // Nothing to do
    }
}
