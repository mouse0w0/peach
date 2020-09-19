package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.element.CraftingRecipe;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.ui.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.ui.util.CachedImage;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.util.FXValidator;
import com.github.mouse0w0.peach.util.ArrayUtils;
import com.github.mouse0w0.peach.wizard.WizardStep;
import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CraftingRecipeStep extends FlowPane implements WizardStep {

    private static final CachedImage BACKGROUND = new CachedImage("/image/mcmod/crafting_recipe.png", 560, 312);

    private final Element<CraftingRecipe> file;

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

    public CraftingRecipeStep(Element<CraftingRecipe> file) {
        this.file = file;
        FXUtils.loadFXML(this, "ui/mcmod/CraftingRecipe.fxml");

        group.setEditable(true);

        FXUtils.setFixedSize(recipeView, 560, 312);
        recipeView.setBackground(new Background(new BackgroundImage(BACKGROUND.getImage(), null, null, null, null)));

        GridPane inputGridPane = new GridPane();
        AnchorPane.setTopAnchor(inputGridPane, 52d);
        AnchorPane.setLeftAnchor(inputGridPane, 52d);
        inputGridPane.setHgap(8);
        inputGridPane.setVgap(8);
        recipeView.getChildren().add(inputGridPane);
        for (int i = 0; i < 9; i++) {
            ItemView itemViews = inputs[i] = new ItemView(64, 64);
            itemViews.setOnMouseClicked(event ->
                    itemViews.setItem(ItemPicker.pick(getScene().getWindow(), itemViews.getItem(), true, true)));
            itemViews.setPlayAnimation(true);
            inputGridPane.add(itemViews, i % 3, i / 3);
        }

        output = new ItemView(64, 64);
        output.setOnMouseClicked(event ->
                output.setItem(ItemPicker.pick(getScene().getWindow(), output.getItem(), false, false)));
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
    public Node getContent() {
        return this;
    }

    @Override
    public void init() {
        CraftingRecipe craftingRecipe = file.get();

        String id1 = craftingRecipe.getId();
        if (Strings.isNullOrEmpty(id1)) id1 = ModUtils.toRegisterName(file.getName());
        id.setText(id1);

        namespace.setValue(craftingRecipe.getNamespace());
        group.setValue(craftingRecipe.getGroup());
        shapeless.setSelected(craftingRecipe.isShapeless());
        ArrayUtils.biForEach(inputs, craftingRecipe.getInputs(), ItemView::setItem);
        ItemStack outputItem = craftingRecipe.getOutput();
        if (outputItem != null) {
            output.setItem(outputItem.getItem());
            outputAmount.getValueFactory().setValue(outputItem.getAmount());
        }
    }

    @Override
    public boolean validate() {
        if (!FXValidator.validate(id, "validate.illegal_recipe_id", ModUtils::isValidRegisterName))
            return false;
        return true;
    }

    @Override
    public void updateDataModel() {
        CraftingRecipe craftingRecipe = file.get();
        craftingRecipe.setId(id.getText());
        craftingRecipe.setNamespace(namespace.getValue());
        craftingRecipe.setGroup(group.getValue());
        craftingRecipe.setShapeless(shapeless.isSelected());
        craftingRecipe.setInputs(ArrayUtils.map(inputs, ItemView::getItem, Item[]::new));
        craftingRecipe.setOutput(new ItemStack(output.getItem(), outputAmount.getValue()));
    }

    @Override
    public void dispose() {
        for (ItemView input : inputs) {
            input.setPlayAnimation(false);
        }
    }
}
