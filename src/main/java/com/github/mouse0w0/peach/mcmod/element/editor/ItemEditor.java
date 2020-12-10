package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.form.ColSpan;
import com.github.mouse0w0.peach.form.Form;
import com.github.mouse0w0.peach.form.FormView;
import com.github.mouse0w0.peach.form.Group;
import com.github.mouse0w0.peach.form.element.*;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.model.json.JsonModel;
import com.github.mouse0w0.peach.mcmod.ui.cell.ItemGroupCell;
import com.github.mouse0w0.peach.mcmod.ui.form.ItemPickerElement;
import com.github.mouse0w0.peach.mcmod.ui.form.ModelTextureElement;
import com.github.mouse0w0.peach.mcmod.ui.form.TextureHandler;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.Check;
import com.github.mouse0w0.peach.ui.util.NotificationLevel;
import javafx.scene.Node;
import javafx.util.StringConverter;

import javax.annotation.Nonnull;

public class ItemEditor extends ElementEditor<ItemElement> {

    private final ContentManager contentManager;

    private Form form;

    // Properties
    private TextFieldElement identifier;
    private TextFieldElement displayName;
    private ComboBoxElement<ItemGroupData> itemGroup;
    private SpinnerElement<Integer> maxStackSize;
    private RadioButtonElement hasEffect;
    private TextAreaElement information;

    // Appearance
    private ChoiceBoxElement<String> model;
    private ModelTextureElement textures;

    public ItemEditor(@Nonnull Project project, @Nonnull ItemElement element) {
        super(project, element);
        contentManager = ContentManager.getInstance(getProject());
    }

    @Override
    protected Node getContent() {
        form = new Form();

        Group properties = new Group();
        properties.setText(I18n.translate("item.properties.title"));
        properties.setCollapsible(false);

        identifier = new TextFieldElement();
        identifier.getChecks().add(new Check<>(ModUtils::isValidRegisterName, NotificationLevel.ERROR, I18n.translate("validate.illegalRegisterName")));
        identifier.setText(I18n.translate("item.properties.identifier"));
        identifier.setColSpan(ColSpan.HALF);

        displayName = new TextFieldElement();
        displayName.setText(I18n.translate("item.properties.displayName"));
        displayName.setColSpan(ColSpan.HALF);

        itemGroup = new ComboBoxElement<>();
        itemGroup.setText(I18n.translate("item.properties.itemGroup"));
        itemGroup.setCellFactory(view -> new ItemGroupCell());
        itemGroup.setButtonCell(new ItemGroupCell());
        itemGroup.getItems().addAll(contentManager.getItemGroups());
        itemGroup.getSelectionModel().selectFirst();
        itemGroup.setColSpan(ColSpan.HALF);

        maxStackSize = new SpinnerElement<>(1, 64, 64);
        maxStackSize.setText(I18n.translate("item.properties.maxStackSize"));
        maxStackSize.setColSpan(ColSpan.HALF);

        hasEffect = new RadioButtonElement();
        hasEffect.setText(I18n.translate("item.properties.hasEffect"));
        hasEffect.setColSpan(ColSpan.HALF);

        information = new TextAreaElement();
        information.setText(I18n.translate("item.properties.information"));

        properties.getElements().addAll(identifier, displayName, itemGroup, maxStackSize, hasEffect, information);

        Group appearance = new Group();
        appearance.setText(I18n.translate("item.appearance.title"));
        appearance.setCollapsible(false);

        model = new ChoiceBoxElement<>();
        model.setText(I18n.translate("item.appearance.model"));
        model.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return I18n.translate("item.model." + object, object);
            }

            @Override
            public String fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        model.getItems().addAll(ModelManager.getInstance(getProject()).getItemModels().keySet());

        textures = new ModelTextureElement(TextureHandler.of(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_TEXTURES)));
        textures.setText(I18n.translate("item.appearance.texture"));
        model.valueProperty().addListener(observable -> {
            JsonModel itemModel = ModelManager.getInstance(getProject()).getItemModel(model.getValue());
            textures.setTextureKeys(itemModel != null ? itemModel.getTextures().keySet() : null);
        });

        appearance.getElements().addAll(model, textures);

        form.getGroups().addAll(properties, appearance);

        return new FormView(form);
    }

    @Override
    protected void initialize(ItemElement element) {
        identifier.setValue(element.getIdentifier());
        displayName.setValue(element.getDisplayName());
        itemGroup.setValue(contentManager.getItemGroup(element.getItemGroup()));
        maxStackSize.setValue(element.getMaxStackSize());
        hasEffect.setValue(element.isHasEffect());
        information.setValue(element.getInformation());
        model.setValue(element.getModel());
        textures.setTextures(element.getTextures());
    }

    @Override
    protected void updateDataModel(ItemElement element) {
        element.setIdentifier(identifier.getValue());
        element.setDisplayName(displayName.getValue());
        element.setItemGroup(itemGroup.getValue().getId());
        element.setMaxStackSize(maxStackSize.getValue());
        element.setHasEffect(hasEffect.getValue());
        element.setInformation(information.getValue());
        element.setModel(model.getValue());
        element.setTextures(textures.getTextures());
    }

    @Override
    protected boolean validate() {
        return form.validate();
    }
}
