package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.form.ColSpan;
import com.github.mouse0w0.peach.form.Form;
import com.github.mouse0w0.peach.form.FormView;
import com.github.mouse0w0.peach.form.Group;
import com.github.mouse0w0.peach.form.field.*;
import com.github.mouse0w0.peach.mcmod.EnchantmentType;
import com.github.mouse0w0.peach.mcmod.EquipmentSlot;
import com.github.mouse0w0.peach.mcmod.ItemType;
import com.github.mouse0w0.peach.mcmod.UseAnimation;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.model.mcj.McjModel;
import com.github.mouse0w0.peach.mcmod.ui.cell.ItemGroupCell;
import com.github.mouse0w0.peach.mcmod.ui.cell.ItemTypeCell;
import com.github.mouse0w0.peach.mcmod.ui.cell.UseAnimationCell;
import com.github.mouse0w0.peach.mcmod.ui.form.*;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.Check;
import com.github.mouse0w0.peach.ui.util.NotificationLevel;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.util.StringConverter;

import javax.annotation.Nonnull;

public class ItemEditor extends ElementEditor<ItemElement> {

    private final ContentManager contentManager;

    private Form form;

    // Properties
    private TextFieldField identifier;
    private TextFieldField displayName;
    private ComboBoxField<ItemType> itemType;
    private ComboBoxField<ItemGroupData> itemGroup;
    private SpinnerField<Integer> maxStackSize;
    private SpinnerField<Integer> durability;
    private SpinnerField<Double> destroySpeed;
    private RadioButtonField canDestroyAnyBlock;
    private ToolAttributesField toolAttributes;
    private AttributeModifiersField attributeModifiers;
    private SpinnerField<Integer> enchantability;
    private CheckComboBoxField<EnchantmentType> acceptableEnchantments;
    private ChoiceBoxField<EquipmentSlot> equipmentSlot;
    private ItemPickerField repairItem;
    private ItemPickerField recipeRemain;
    private ComboBoxField<UseAnimation> useAnimation;
    private SpinnerField<Integer> useDuration;
    private TextAreaField information;

    // Appearance
    private ChoiceBoxField<String> model;
    private ModelTextureField textures;
    private RadioButtonField hasEffect;

    // Extra
    private SpinnerField<Integer> fuelBurnTime;
    private SpinnerField<Double> hunger;
    private SpinnerField<Double> saturation;
    private RadioButtonField isWolfFood;
    private RadioButtonField alwaysEdible;
    private ItemPickerField foodContainer;

    public ItemEditor(@Nonnull Project project, @Nonnull ItemElement element) {
        super(project, element);
        contentManager = ContentManager.getInstance(getProject());
    }

    @Override
    protected Node getContent() {
        form = new Form();

        Group properties = new Group();
        properties.setText(I18n.translate("item.properties.title"));

        identifier = new TextFieldField();
        identifier.getChecks().add(new Check<>(ModUtils::isValidRegisterName, NotificationLevel.ERROR, I18n.translate("validate.illegalRegisterName")));
        identifier.setText(I18n.translate("item.properties.identifier"));
        identifier.setColSpan(ColSpan.HALF);

        displayName = new TextFieldField();
        displayName.setText(I18n.translate("item.properties.displayName"));
        displayName.setColSpan(ColSpan.HALF);

        itemType = new ComboBoxField<>();
        itemType.setText(I18n.translate("item.properties.itemType"));
        itemType.setCellFactory(view -> new ItemTypeCell());
        itemType.setButtonCell(new ItemTypeCell());
        itemType.getItems().setAll(ItemType.values());
        itemType.setColSpan(ColSpan.HALF);

        itemGroup = new ComboBoxField<>();
        itemGroup.setText(I18n.translate("item.properties.itemGroup"));
        itemGroup.setCellFactory(view -> new ItemGroupCell());
        itemGroup.setButtonCell(new ItemGroupCell());
        itemGroup.getItems().setAll(contentManager.getItemGroups());
        itemGroup.setColSpan(ColSpan.HALF);

        maxStackSize = new SpinnerField<>(1, 64, 64);
        maxStackSize.setText(I18n.translate("item.properties.maxStackSize"));
        maxStackSize.setColSpan(ColSpan.HALF);

        durability = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        durability.setText(I18n.translate("item.properties.durability"));
        durability.setColSpan(ColSpan.HALF);

        destroySpeed = new SpinnerField<>(0.0, Float.MAX_VALUE, 0.0);
        destroySpeed.setText(I18n.translate("item.properties.destroySpeed"));
        destroySpeed.setColSpan(ColSpan.HALF);

        canDestroyAnyBlock = new RadioButtonField();
        canDestroyAnyBlock.setText(I18n.translate("item.properties.canDestroyAnyBlock"));
        canDestroyAnyBlock.setColSpan(ColSpan.HALF);

        toolAttributes = new ToolAttributesField();
        toolAttributes.setText(I18n.translate("item.properties.toolAttributes"));
        toolAttributes.setColSpan(ColSpan.HALF);

        attributeModifiers = new AttributeModifiersField();
        attributeModifiers.setText(I18n.translate("item.properties.attributeModifiers"));

        enchantability = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        enchantability.setText(I18n.translate("item.properties.enchantability"));
        enchantability.setColSpan(ColSpan.HALF);

        acceptableEnchantments = new CheckComboBoxField<>();
        acceptableEnchantments.setText(I18n.translate("item.properties.acceptableEnchantments"));
        acceptableEnchantments.getItems().setAll(EnchantmentType.values());
        acceptableEnchantments.setConverter(new StringConverter<EnchantmentType>() {
            @Override
            public String toString(EnchantmentType object) {
                return object.getLocalizedName();
            }

            @Override
            public EnchantmentType fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        acceptableEnchantments.setColSpan(ColSpan.HALF);

        equipmentSlot = new ChoiceBoxField<>();
        equipmentSlot.setText(I18n.translate("item.properties.equipmentSlot"));
        equipmentSlot.setConverter(new StringConverter<EquipmentSlot>() {
            @Override
            public String toString(EquipmentSlot object) {
                return object.getLocalizedName();
            }

            @Override
            public EquipmentSlot fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        equipmentSlot.getItems().setAll(EquipmentSlot.values());
        equipmentSlot.setColSpan(ColSpan.HALF);

        repairItem = new ItemPickerField();
        repairItem.setText(I18n.translate("item.properties.repairItem"));
        repairItem.setFitSize(32, 32);
        repairItem.setColSpan(ColSpan.HALF);

        recipeRemain = new ItemPickerField();
        recipeRemain.setText(I18n.translate("item.properties.recipeRemain"));
        recipeRemain.setFitSize(32, 32);
        recipeRemain.setColSpan(ColSpan.HALF);

        useAnimation = new ComboBoxField<>();
        useAnimation.setText(I18n.translate("item.properties.useAnimation"));
        useAnimation.setCellFactory(view -> new UseAnimationCell());
        useAnimation.setButtonCell(new UseAnimationCell());
        useAnimation.getItems().setAll(UseAnimation.values());
        useAnimation.setColSpan(ColSpan.HALF);

        useDuration = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        useDuration.setText(I18n.translate("item.properties.useDuration"));
        useDuration.setColSpan(ColSpan.HALF);

        information = new TextAreaField();
        information.setText(I18n.translate("item.properties.information"));

        properties.getElements().addAll(
                identifier, displayName,
                itemType, itemGroup,
                maxStackSize, durability,
                equipmentSlot, toolAttributes,
                destroySpeed, canDestroyAnyBlock,
                enchantability, acceptableEnchantments,
                attributeModifiers,
                repairItem, recipeRemain,
                useAnimation, useDuration,
                information);

        Group appearance = new Group();
        appearance.setText(I18n.translate("item.appearance.title"));

        model = new ChoiceBoxField<>();
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

        textures = new ModelTextureField(TextureHandler.of(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_TEXTURES)));
        textures.setText(I18n.translate("item.appearance.texture"));
        model.valueProperty().addListener(observable -> {
            McjModel itemModel = ModelManager.getInstance(getProject()).getItemModel(model.getValue());
            textures.setTextureKeys(itemModel != null ? itemModel.getTextures().keySet() : null);
        });

        hasEffect = new RadioButtonField();
        hasEffect.setText(I18n.translate("item.appearance.hasEffect"));
        hasEffect.setColSpan(ColSpan.HALF);

        appearance.getElements().addAll(model, textures, hasEffect);

        Group extra = new Group();
        extra.setText(I18n.translate("item.extra.title"));

        fuelBurnTime = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        fuelBurnTime.setText(I18n.translate("item.fuel.fuelBurnTime"));
        fuelBurnTime.setColSpan(ColSpan.HALF);

        BooleanBinding isFood = itemType.valueProperty().isEqualTo(ItemType.FOOD);

        hunger = new SpinnerField<>(0.0, 20.0, 0.0);
        hunger.setText(I18n.translate("item.food.hunger"));
        hunger.setColSpan(ColSpan.HALF);
        hunger.visibleProperty().bind(isFood);

        saturation = new SpinnerField<>(0.0, Double.MAX_VALUE, 0.0);
        saturation.setText(I18n.translate("item.food.saturation"));
        saturation.setColSpan(ColSpan.HALF);
        saturation.visibleProperty().bind(isFood);

        isWolfFood = new RadioButtonField();
        isWolfFood.setText(I18n.translate("item.food.isWolfFood"));
        isWolfFood.setColSpan(ColSpan.HALF);
        isWolfFood.visibleProperty().bind(isFood);

        alwaysEdible = new RadioButtonField();
        alwaysEdible.setText(I18n.translate("item.food.alwaysEdible"));
        alwaysEdible.setColSpan(ColSpan.HALF);
        alwaysEdible.visibleProperty().bind(isFood);

        extra.getElements().addAll(fuelBurnTime, hunger, saturation, isWolfFood, alwaysEdible);

        form.getGroups().addAll(properties, appearance, extra);

        return new FormView(form);
    }

    @Override
    protected void initialize(ItemElement element) {
        identifier.setValue(element.getIdentifier());
        displayName.setValue(element.getDisplayName());
        itemType.setValue(element.getItemType());
        itemGroup.setValue(contentManager.getItemGroup(element.getItemGroup()));
        maxStackSize.setValue(element.getMaxStackSize());
        durability.setValue(element.getDurability());
        destroySpeed.setValue(element.getDestroySpeed());
        canDestroyAnyBlock.setValue(element.isCanDestroyAnyBlock());
        toolAttributes.setValue(element.getToolAttributes());
        attributeModifiers.setValue(element.getAttributeModifiers());
        enchantability.setValue(element.getEnchantability());
        acceptableEnchantments.setValue(element.getAcceptableEnchantments());
        equipmentSlot.setValue(element.getEquipmentSlot());
        repairItem.setValue(element.getRepairItem());
        recipeRemain.setValue(element.getRecipeRemain());
        useAnimation.setValue(element.getUseAnimation());
        useDuration.setValue(element.getUseDuration());
        fuelBurnTime.setValue(element.getFuelBurnTime());
        hasEffect.setValue(element.isHasEffect());
        information.setValue(element.getInformation());
        model.setValue(element.getModel());
        textures.setTextures(element.getTextures());
    }

    @Override
    protected void updateDataModel(ItemElement element) {
        element.setIdentifier(identifier.getValue());
        element.setDisplayName(displayName.getValue());
        element.setItemType(itemType.getValue());
        element.setItemGroup(itemGroup.getValue().getId());
        element.setMaxStackSize(maxStackSize.getValue());
        element.setDurability(durability.getValue());
        element.setDestroySpeed(destroySpeed.getValue());
        element.setCanDestroyAnyBlock(canDestroyAnyBlock.getValue());
        element.setToolAttributes(toolAttributes.getValue());
        element.setAttributeModifiers(attributeModifiers.getValue());
        element.setEnchantability(enchantability.getValue());
        element.setAcceptableEnchantments(acceptableEnchantments.getValue(EnchantmentType[]::new));
        element.setEquipmentSlot(equipmentSlot.getValue());
        element.setRepairItem(repairItem.getValue());
        element.setRecipeRemain(recipeRemain.getValue());
        element.setUseAnimation(useAnimation.getValue());
        element.setUseDuration(useDuration.getValue());
        element.setFuelBurnTime(fuelBurnTime.getValue());
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
