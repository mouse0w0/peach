package com.github.mouse0w0.peach.ui.forge;

import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.util.ImageCache;
import com.github.mouse0w0.peach.ui.wizard.WizardStep;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CraftingRecipeUI extends FlowPane implements WizardStep {

    private static final ImageCache.Key IMAGE_KEY = new ImageCache.Key("/image/forge/crafting_recipe.png", 560, 312);

    @FXML
    private TextField id;
    @FXML
    private ChoiceBox<String> namespace;
    @FXML
    private ComboBox<String> group;
    @FXML
    private RadioButton shapeless;
    @FXML
    private AnchorPane recipeView;

    private ItemView[] inputs = new ItemView[9];
    private ItemView output;
    private Spinner<Integer> outputAmount;

    public CraftingRecipeUI() {
        FXUtils.loadFXML(this, "ui/forge/CraftingRecipe.fxml");

        group.setEditable(true);

        FXUtils.setFixedSize(recipeView, 560, 312);
        recipeView.setBackground(new Background(new BackgroundImage(ImageCache.getImage(IMAGE_KEY), null, null, null, null)));

        GridPane inputGridPane = new GridPane();
        AnchorPane.setTopAnchor(inputGridPane, 52d);
        AnchorPane.setLeftAnchor(inputGridPane, 52d);
        inputGridPane.setHgap(8);
        inputGridPane.setVgap(8);
        recipeView.getChildren().add(inputGridPane);
        for (int i = 0; i < 9; i++) {
            ItemView itemViews = inputs[i] = new ItemView();
            itemViews.setPickOnBounds(true);
            itemViews.setOnMouseClicked(event -> itemViews.setItemToken(ItemPicker.show(getScene().getWindow()).getSelectedItem()));
            itemViews.setPlayAnimation(true);
            itemViews.setFitSize(64, 64);
            inputGridPane.add(itemViews, i % 3, i / 3);
        }

        output = new ItemView();
        output.setPickOnBounds(true);
        output.setOnMouseClicked(event -> output.setItemToken(ItemPicker.show(getScene().getWindow()).getSelectedItem()));
        output.setFitSize(64, 64);
        AnchorPane.setTopAnchor(output, 125d);
        AnchorPane.setLeftAnchor(output, 428d);
        recipeView.getChildren().add(output);

        outputAmount = new Spinner<>(1, 127, 1);
        outputAmount.setEditable(true);
        FXUtils.setFixedSize(outputAmount, 104, 24);
        AnchorPane.setTopAnchor(outputAmount, 220d);
        AnchorPane.setLeftAnchor(outputAmount, 408d);
        recipeView.getChildren().add(outputAmount);
    }

    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public void dispose() {
        for (ItemView input : inputs) {
            input.setPlayAnimation(false);
        }
    }
}
