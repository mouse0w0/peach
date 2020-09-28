package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.ui.cell.ItemGroupCell;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.util.FXValidator;
import com.github.mouse0w0.peach.wizard.WizardStep;
import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

public class ItemStep extends FlowPane implements WizardStep {

    private final ItemElement element;

    @FXML
    private TextField registerName;
    @FXML
    private TextField displayName;
    @FXML
    private ComboBox<ItemGroupData> itemGroup;
    @FXML
    private Spinner<Integer> maxStackSize;
    @FXML
    private RadioButton effect;
    @FXML
    private TextArea information;

    public ItemStep(ItemElement element) {
        this.element = element;

        FXUtils.loadFXML(this, "ui/mcmod/ItemElement.fxml");

        maxStackSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 64, 64));

        Project project = WindowManager.getInstance().getFocusedWindow().getProject();
        ContentManager contentManager = ContentManager.getInstance(project);

        itemGroup.setCellFactory(listView -> new ItemGroupCell());
        itemGroup.getItems().addAll(contentManager.getItemGroupMap().values());
        itemGroup.setButtonCell(new ItemGroupCell());
        itemGroup.getSelectionModel().selectFirst();
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public void init() {
        Project project = WindowManager.getInstance().getFocusedWindow().getProject();
        ContentManager contentManager = ContentManager.getInstance(project);

        registerName.setText(Strings.isNullOrEmpty(element.getRegisterName()) ?
                ModUtils.toRegisterName(element.getFileName()) : element.getRegisterName());
        displayName.setText(Strings.isNullOrEmpty(element.getDisplayName()) ?
                element.getFileName() : element.getDisplayName());
        ItemGroupData itemGroupData = contentManager.getItemGroup(element.getItemGroup());
        if (itemGroupData != null) itemGroup.getSelectionModel().select(itemGroupData);
        else itemGroup.getSelectionModel().selectFirst();
        maxStackSize.getValueFactory().setValue(element.getMaxStackSize());
        effect.setSelected(element.isEffect());
        information.setText(element.getInformation());
    }

    @Override
    public boolean validate() {
        if (!FXValidator.validate(registerName, "validate.illegal_register_name", ModUtils::isValidRegisterName))
            return false;
        return true;
    }

    @Override
    public void updateDataModel() {
        element.setRegisterName(registerName.getText());
        element.setDisplayName(displayName.getText());
        element.setItemGroup(itemGroup.getValue().getId());
        element.setMaxStackSize(maxStackSize.getValue());
        element.setEffect(effect.isSelected());
        element.setInformation(information.getText());
    }

    @Override
    public void dispose() {
        // Nothing to do
    }
}
