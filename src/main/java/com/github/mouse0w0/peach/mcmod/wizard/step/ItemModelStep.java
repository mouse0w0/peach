package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.model.json.JsonModel;
import com.github.mouse0w0.peach.mcmod.ui.control.TextureView;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.wizard.WizardStep;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ItemModelStep extends FlowPane implements WizardStep {

    private final ItemElement element;
    private final Project project;

    @FXML
    private ChoiceBox<String> model;
    @FXML
    private GridPane content;

    private Map<String, TextureView> textureViewMap = new HashMap<>();

    public ItemModelStep(ItemElement element) {
        this.element = element;
        this.project = WindowManager.getInstance().getFocusedProject();

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
        model.getItems().addAll(modelManager.getItemModels().keySet());
        model.valueProperty().addListener(observable -> {
            clearTextures();

            JsonModel model = modelManager.getItemModel(this.model.getValue());
            if (model == null) return;

            Set<String> textures = model.getTextures().keySet();
            int row = 2;
            for (String texture : textures) {
                content.add(new Text(texture), 0, row);

                TextureView textureView = new TextureView();
                FXUtils.setFixedSize(textureView, 64, 64);
                textureView.setProject(project);
                textureView.setFitSize(64, 64);
                content.add(textureView, 1, row);
                textureViewMap.put(texture, textureView);

                row++;
            }
        });
    }


    private void clearTextures() {
        ObservableList<Node> children = content.getChildren();
        if (children.size() > 3) children.remove(3, children.size());

        textureViewMap.clear();
    }

    private void setTexture(String texture, String fileName) {
        textureViewMap.get(texture).setTexture(fileName);
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public void init() {
        model.getSelectionModel().select(element.getModel());
        if (model.getSelectionModel().isEmpty()) model.getSelectionModel().select("generated");

        element.getTextures().forEach(this::setTexture);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void updateDataModel() {
        element.setModel(model.getValue());
        Map<String, String> textures = new LinkedHashMap<>();
        textureViewMap.forEach((key, value) -> textures.put(key, value.getTexture()));
        element.setTextures(textures);
    }

    @Override
    public void dispose() {
        // Nothing to do
    }
}
