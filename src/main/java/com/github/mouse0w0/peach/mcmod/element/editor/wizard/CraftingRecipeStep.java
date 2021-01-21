package com.github.mouse0w0.peach.mcmod.element.editor.wizard;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.javafx.CachedImage;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.javafx.Validator;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.element.impl.CraftingRecipe;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemStackView;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.util.ArrayUtils;
import com.github.mouse0w0.peach.wizard.WizardStepBase;
import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
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

    private ItemPicker[] inputs = new ItemPicker[9];
    private ItemStackView output;

    public CraftingRecipeStep(CraftingRecipe element) {
        this.element = element;
        setContent(FXUtils.loadFXML(null, this, "ui/mcmod/CraftingRecipe.fxml"));

        Validator.error(id, ModUtils::isValidIdentifier, I18n.translate("validate.illegalIdentifier"));

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
            ItemView itemViews = inputs[i] = new ItemPicker(64, 64, true, true);
            itemViews.setPlayAnimation(true);
            inputGridPane.add(itemViews, i % 3, i / 3);
        }

        output = new ItemStackView();
        output.setFitSize(64, 64);
        FXUtils.setFixedSize(output, 72, 72);
        AnchorPane.setTopAnchor(output, 121d);
        AnchorPane.setLeftAnchor(output, 424d);
        recipeView.getChildren().add(output);
    }

    @Override
    public void init() {
        String id1 = element.getIdentifier();
        if (Strings.isNullOrEmpty(id1)) id1 = ModUtils.toIdentifier(this.element.getFileName());
        id.setText(id1);

        namespace.setValue(element.getNamespace());
        group.setValue(element.getGroup());
        shapeless.setSelected(element.isShapeless());
        ArrayUtils.biForEach(inputs, element.getInputs(), ItemView::setItem);
        output.setItemStack(element.getOutput());
    }

    @Override
    public boolean validate() {
        return Validator.test(id);
    }

    @Override
    public void updateDataModel() {
        element.setIdentifier(id.getText());
        element.setNamespace(namespace.getValue());
        element.setGroup(group.getValue());
        element.setShapeless(shapeless.isSelected());
        element.setInputs(ArrayUtils.map(inputs, ItemView::getItem, ItemRef[]::new));
        element.setOutput(output.getItemStack());
    }

    @Override
    public void dispose() {
        for (ItemView input : inputs) {
            input.setPlayAnimation(false);
        }
    }
}
