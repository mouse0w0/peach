package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroup;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.wizard.WizardStep;
import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ItemGroupStep extends FlowPane implements WizardStep {

    private final ItemGroup element;

    @FXML
    private GridPane content;

    @FXML
    private TextField registerName;
    @FXML
    private TextField displayName;

    private ItemView icon;

    public ItemGroupStep(ItemGroup element) {
        this.element = element;

        FXUtils.loadFXML(this, "ui/mcmod/ItemGroup.fxml");

        icon = new ItemPicker(32, 32);
        FXUtils.setFixedSize(icon, 32, 32);
        icon.setBackground(new Background(new BackgroundFill(Color.gray(0.8), null, null)));
        content.add(icon, 1, 2);
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public void init() {
        registerName.setText(Strings.isNullOrEmpty(element.getRegisterName()) ?
                ModUtils.toRegisterName(element.getFileName()) : element.getRegisterName());
        displayName.setText(Strings.isNullOrEmpty(element.getDisplayName()) ?
                element.getFileName() : element.getDisplayName());
        icon.setItem(element.getIcon());
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void updateDataModel() {
        element.setRegisterName(registerName.getText());
        element.setDisplayName(displayName.getText());
        element.setIcon(icon.getItem());
    }

    @Override
    public void dispose() {

    }
}
