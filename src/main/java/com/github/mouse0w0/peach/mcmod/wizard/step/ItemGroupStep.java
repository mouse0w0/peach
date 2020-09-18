package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ItemGroup;
import com.github.mouse0w0.peach.mcmod.ui.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.wizard.WizardStep;
import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class ItemGroupStep extends FlowPane implements WizardStep {

    private final Element<ItemGroup> element;

    @FXML
    private GridPane content;

    @FXML
    private TextField registerName;
    @FXML
    private TextField displayName;

    private ItemView icon;

    public ItemGroupStep(Element<ItemGroup> element) {
        this.element = element;

        FXUtils.loadFXML(this, "ui/mcmod/ItemGroup.fxml");

        icon = new ItemView(32, 32);
        icon.setOnMouseClicked(event ->
                icon.setItem(ItemPicker.pick(getScene().getWindow(), icon.getItem(), false, false)));
        content.add(icon, 1, 2);
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public void init() {
        ItemGroup itemGroup = element.get();
        registerName.setText(Strings.isNullOrEmpty(itemGroup.getRegisterName()) ?
                ModUtils.toRegisterName(element.getName()) : itemGroup.getRegisterName());
        displayName.setText(Strings.isNullOrEmpty(itemGroup.getDisplayName()) ?
                element.getName() : itemGroup.getDisplayName());
        icon.setItem(itemGroup.getIcon());
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void updateDataModel() {
        ItemGroup itemGroup = element.get();
        itemGroup.setRegisterName(registerName.getText());
        itemGroup.setDisplayName(displayName.getText());
        itemGroup.setIcon(icon.getItem());
    }

    @Override
    public void dispose() {

    }
}
