package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingElement;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemStackView;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.NotNull;

public class SmeltingRecipeEditor extends ElementEditor<SmeltingElement> {
    private static final Image BACKGROUND = new Image("/image/mcmod/smelting_recipe.png", 560, 312, true, false, true);

    @FXML
    private Spinner<Double> xp;
    @FXML
    private AnchorPane recipeView;
    private ItemPicker input;
    private ItemStackView output;

    public SmeltingRecipeEditor(@NotNull Project project, @NotNull SmeltingElement element) {
        super(project, element);
    }

    @Override
    protected Node getContent() {
        FlowPane root = FXUtils.loadFXML(null, this, "ui/mcmod/SmeltingRecipe.fxml", AppL10n.getResourceBundle());

        xp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 0.1));

        FXUtils.setFixedSize(recipeView, 560, 312);
        recipeView.setBackground(new Background(new BackgroundImage(BACKGROUND, null, null, null, null)));

        input = new ItemPicker(getProject(), 64, true, false);
        input.setPlayAnimation(true);
        AnchorPane.setTopAnchor(input, 52d);
        AnchorPane.setLeftAnchor(input, 120d);
        recipeView.getChildren().add(input);

        output = new ItemStackView(getProject(), 64);
        FXUtils.setFixedSize(output, 72, 72);
        AnchorPane.setTopAnchor(output, 120d);
        AnchorPane.setLeftAnchor(output, 356d);
        recipeView.getChildren().add(output);

        return root;
    }

    @Override
    protected void initialize(SmeltingElement element) {
        xp.getValueFactory().setValue(element.getXp());
        input.setItem(element.getInput());
        output.setItemStack(element.getOutput());
    }

    @Override
    protected void updateDataModel(SmeltingElement element) {
        element.setXp(xp.getValue());
        element.setInput(input.getItem());
        element.setOutput(output.getItemStack());
    }

    @Override
    public void dispose() {
        super.dispose();
        input.setPlayAnimation(false);
    }
}
