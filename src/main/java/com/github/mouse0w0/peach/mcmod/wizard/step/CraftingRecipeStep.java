package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.dialog.ItemPicker;
import com.github.mouse0w0.peach.mcmod.element.impl.CraftingRecipe;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.ui.util.CachedImage;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.util.ArrayUtils;
import com.github.mouse0w0.peach.wizard.WizardStepBase;
import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;

public class CraftingRecipeStep extends WizardStepBase {

    private static final CachedImage BACKGROUND = new CachedImage("/image/mcmod/crafting_recipe.png", 560, 312);

    private final CraftingRecipe element;

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

    public CraftingRecipeStep(CraftingRecipe element) {
        this.element = element;
        setContent(FXUtils.loadFXML(null, this, "ui/mcmod/CraftingRecipe.fxml"));

        register(id, ModUtils::isValidRegisterName, I18n.translate("validate.illegal_recipe_id"));

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
                    itemViews.setItem(ItemPicker.pick(getContent(), itemViews.getItem(), true, true)));
            itemViews.setPlayAnimation(true);
            inputGridPane.add(itemViews, i % 3, i / 3);
        }

        output = new ItemView(64, 64);
        output.setOnMouseClicked(event ->
                output.setItem(ItemPicker.pick(getContent(), output.getItem(), false, false)));
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
    public void init() {
        String id1 = element.getId();
        if (Strings.isNullOrEmpty(id1)) id1 = ModUtils.toRegisterName(this.element.getFileName());
        id.setText(id1);

        namespace.setValue(element.getNamespace());
        group.setValue(element.getGroup());
        shapeless.setSelected(element.isShapeless());
        ArrayUtils.biForEach(inputs, element.getInputs(), ItemView::setItem);
        ItemStack outputItem = element.getOutput();
        if (outputItem != null) {
            output.setItem(outputItem.getItem());
            outputAmount.getValueFactory().setValue(outputItem.getAmount());
        }
    }

    @Override
    public void updateDataModel() {
        element.setId(id.getText());
        element.setNamespace(namespace.getValue());
        element.setGroup(group.getValue());
        element.setShapeless(shapeless.isSelected());
        element.setInputs(ArrayUtils.map(inputs, ItemView::getItem, Item[]::new));
        element.setOutput(new ItemStack(output.getItem(), outputAmount.getValue()));
    }

    @Override
    public void dispose() {
        for (ItemView input : inputs) {
            input.setPlayAnimation(false);
        }
    }
}
