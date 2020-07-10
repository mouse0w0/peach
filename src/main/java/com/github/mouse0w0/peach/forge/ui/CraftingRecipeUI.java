package com.github.mouse0w0.peach.forge.ui;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.forge.Item;
import com.github.mouse0w0.peach.forge.ItemStack;
import com.github.mouse0w0.peach.forge.element.CraftingRecipe;
import com.github.mouse0w0.peach.forge.element.ElementFile;
import com.github.mouse0w0.peach.forge.util.ModUtils;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.util.ImageCache;
import com.github.mouse0w0.peach.ui.util.Message;
import com.github.mouse0w0.peach.ui.wizard.WizardStep;
import com.github.mouse0w0.peach.util.ArrayUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CraftingRecipeUI extends FlowPane implements WizardStep {

    private static final ImageCache.Key IMAGE_KEY = new ImageCache.Key("/image/forge/crafting_recipe.png", 560, 312);

    private final ElementFile<CraftingRecipe> file;

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

    public CraftingRecipeUI(ElementFile<CraftingRecipe> file) {
        this.file = file;
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
            itemViews.setOnMouseClicked(event -> {
                ItemPicker itemPicker = ItemPicker.show(getScene().getWindow(), false);
                if (itemPicker.isCancelled()) return;
                itemViews.setItem(itemPicker.getSelectedItem());
            });
            itemViews.setPlayAnimation(true);
            itemViews.setFitSize(64, 64);
            inputGridPane.add(itemViews, i % 3, i / 3);
        }

        output = new ItemView();
        output.setPickOnBounds(true);
        output.setOnMouseClicked(event -> {
            ItemPicker itemPicker = ItemPicker.show(getScene().getWindow(), true);
            if (itemPicker.isCancelled()) return;
            output.setItem(itemPicker.getSelectedItem());
        });
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
    public Node getContent() {
        return this;
    }

    @Override
    public void init() {
        CraftingRecipe craftingRecipe = file.get();

        String recipeId = craftingRecipe.getId();
        if (Strings.isNullOrEmpty(recipeId)) {
            String fileName = FileUtils.getFileNameWithoutExtensionName(file.getFile());
            String standardRecipeId = ModUtils.toRegisterName(fileName);
            if (ModUtils.REGISTER_NAME.matcher(standardRecipeId).matches()) {
                recipeId = standardRecipeId;
            }
        }

        id.setText(recipeId);
        namespace.setValue(craftingRecipe.getNamespace());
        group.setValue(craftingRecipe.getGroup());
        shapeless.setSelected(craftingRecipe.isShapeless());
        ArrayUtils.biForEach(inputs, craftingRecipe.getInputs(), ItemView::setItem);
        ItemStack output = craftingRecipe.getOutput();
        if (output != null) {
            this.output.setItem(output.getItem());
            outputAmount.getValueFactory().setValue(output.getAmount());
        }
    }

    @Override
    public boolean validate() {
        if (!ModUtils.REGISTER_NAME.matcher(id.getText()).matches()) {
            Message.warning(I18n.translate("ui.crafting_recipe.warning.id"));
            id.requestFocus();
            id.selectAll();
            return false;
        }
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
