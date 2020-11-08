package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemStackView;
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

    private ItemPicker input;
    private ItemStackView output;

    public SmeltingRecipeStep(SmeltingRecipe element) {
        this.element = element;
        FXUtils.loadFXML(this, "ui/mcmod/SmeltingRecipe.fxml");

        xp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 0.1));

        FXUtils.setFixedSize(recipeView, 560, 312);
        recipeView.setBackground(new Background(
                new BackgroundImage(BACKGROUND.getImage(), null, null, null, null)));

        input = new ItemPicker(64, 64, true, false);
        input.setPlayAnimation(true);
        AnchorPane.setTopAnchor(input, 52d);
        AnchorPane.setLeftAnchor(input, 120d);
        recipeView.getChildren().add(input);

        output = new ItemStackView();
        output.setFitSize(64, 64);
        FXUtils.setFixedSize(output, 72, 72);
        AnchorPane.setTopAnchor(output, 120d);
        AnchorPane.setLeftAnchor(output, 356d);
        recipeView.getChildren().add(output);
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public void init() {
        xp.getValueFactory().setValue(element.getXp());
        input.setItem(element.getInput());
        output.setItemStack(element.getOutput());
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void updateDataModel() {
        element.setXp(xp.getValue());
        element.setInput(input.getItem());
        element.setOutput(output.getItemStack());
    }

    @Override
    public void dispose() {
        input.setPlayAnimation(false);
    }
}
