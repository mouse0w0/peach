package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.ui.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.ItemView;
import com.github.mouse0w0.peach.ui.util.CachedImage;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.wizard.WizardStep;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.FlowPane;

public class SmeltingRecipeStep extends FlowPane implements WizardStep {

    private static final CachedImage BACKGROUND = new CachedImage("/image/forge/smelting_recipe.png", 560, 312);

    private final Element<SmeltingRecipe> file;

    @FXML
    private Spinner<Double> xp;

    @FXML
    private AnchorPane recipeView;

    private ItemView input;
    private ItemView output;
    private Spinner<Integer> outputAmount;

    public SmeltingRecipeStep(Element<SmeltingRecipe> file) {
        this.file = file;
        FXUtils.loadFXML(this, "ui/mcmod/SmeltingRecipe.fxml");

        xp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 0.1));

        FXUtils.setFixedSize(recipeView, 560, 312);
        recipeView.setBackground(new Background(
                new BackgroundImage(BACKGROUND.getImage(), null, null, null, null)));

        input = new ItemView(64, 64);
        input.setPickOnBounds(true);
        input.setOnMouseClicked(event ->
                input.setItem(ItemPicker.pick(getScene().getWindow(), input.getItem(), true, false)));
        input.setPlayAnimation(true);
        AnchorPane.setTopAnchor(input, 52d);
        AnchorPane.setLeftAnchor(input, 120d);
        recipeView.getChildren().add(input);

        output = new ItemView(64, 64);
        output.setPickOnBounds(true);
        output.setOnMouseClicked(event ->
                output.setItem(ItemPicker.pick(getScene().getWindow(), output.getItem(), true, false)));
        AnchorPane.setTopAnchor(output, 124d);
        AnchorPane.setLeftAnchor(output, 360d);
        recipeView.getChildren().add(output);

        outputAmount = new Spinner<>(1, 127, 1);
        outputAmount.setEditable(true);
        FXUtils.setFixedSize(outputAmount, 104, 24);
        AnchorPane.setTopAnchor(outputAmount, 220d);
        AnchorPane.setLeftAnchor(outputAmount, 340d);
        recipeView.getChildren().add(outputAmount);
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public void init() {
        SmeltingRecipe recipe = file.get();
        xp.getValueFactory().setValue(recipe.getXp());
        input.setItem(recipe.getInput());
        ItemStack outputItem = recipe.getOutput();
        if (outputItem != null) {
            output.setItem(outputItem.getItem());
            outputAmount.getValueFactory().setValue(outputItem.getAmount());
        }
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void updateDataModel() {
        SmeltingRecipe recipe = file.get();
        recipe.setXp(xp.getValue());
        recipe.setInput(input.getItem());
        recipe.setOutput(new ItemStack(output.getItem(), outputAmount.getValue()));
    }

    @Override
    public void dispose() {
        input.setPlayAnimation(false);
    }
}
