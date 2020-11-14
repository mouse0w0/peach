package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.ui.cell.ItemGroupCell;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.wizard.WizardStepBase;
import com.google.common.base.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ItemStep extends WizardStepBase {

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

        setContent(FXUtils.loadFXML(null, this, "ui/mcmod/ItemElement.fxml"));

        register(registerName, ModUtils::isValidRegisterName, I18n.translate("validate.illegal_register_name"));

        maxStackSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 64, 64));

        Project project = WindowManager.getInstance().getFocusedProject();
        ContentManager contentManager = ContentManager.getInstance(project);

        itemGroup.setCellFactory(listView -> new ItemGroupCell());
        itemGroup.getItems().addAll(contentManager.getItemGroups());
        itemGroup.setButtonCell(new ItemGroupCell());
        itemGroup.getSelectionModel().selectFirst();
    }

    @Override
    public void init() {
        Project project = WindowManager.getInstance().getFocusedProject();
        ContentManager contentManager = ContentManager.getInstance(project);

        registerName.setText(Strings.isNullOrEmpty(element.getRegisterName()) ?
                ModUtils.toRegisterName(element.getFileName()) : element.getRegisterName());
        displayName.setText(Strings.isNullOrEmpty(element.getDisplayName()) ?
                element.getFileName() : element.getDisplayName());
        ItemGroupData itemGroupData = contentManager.getItemGroup(element.getItemGroup());
        if (itemGroupData != null) itemGroup.getSelectionModel().select(itemGroupData);
        else itemGroup.getSelectionModel().selectFirst();
        maxStackSize.getValueFactory().setValue(element.getMaxStackSize());
        effect.setSelected(element.isHasEffect());
        information.setText(element.getInformation());
    }

    @Override
    public void updateDataModel() {
        element.setRegisterName(registerName.getText());
        element.setDisplayName(displayName.getText());
        element.setItemGroup(itemGroup.getValue().getId());
        element.setMaxStackSize(maxStackSize.getValue());
        element.setHasEffect(effect.isSelected());
        element.setInformation(information.getText());
    }

    @Override
    public void dispose() {
        // Nothing to do
    }
}
