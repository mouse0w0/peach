package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.dialog.ItemPicker;
import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
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

    private static final CachedImage BACKGROUND = new CachedImage("/image/mcmod/smelting_recipe.png", 560, 312);

    private final SmeltingRecipe element;

    @FXML
    private Spinner<Double> xp;

    @FXML
    private AnchorPane recipeView;

    private ItemView input;
    private ItemView output;
    private Spinner<Integer> outputAmount;

    public SmeltingRecipeStep(SmeltingRecipe element) {
        this.element = element;
        FXUtils.loadFXML(this, "ui/mcmod/SmeltingRecipe.fxml");

        xp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 0.1));

        FXUtils.setFixedSize(recipeView, 560, 312);
        recipeView.setBackground(new Background(
                new BackgroundImage(BACKGROUND.getImage(), null, null, null, null)));

        input = new ItemView(64, 64);
        input.setOnMouseClicked(event ->
                input.setItem(ItemPicker.pick(getScene().getWindow(), input.getItem(), true, false)));
        input.setPlayAnimation(true);
        AnchorPane.setTopAnchor(input, 52d);
        AnchorPane.setLeftAnchor(input, 120d);
        recipeView.getChildren().add(input);

        output = new ItemView(64, 64);
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
        xp.getValueFactory().setValue(element.getXp());
        input.setItem(element.getInput());
        ItemStack outputItem = element.getOutput();
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
        element.setXp(xp.getValue());
        element.setInput(input.getItem());
        element.setOutput(new ItemStack(output.getItem(), outputAmount.getValue()));
    }

    @Override
    public void dispose() {
        input.setPlayAnimation(false);
    }
}
