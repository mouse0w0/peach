package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.form.ColSpan;
import com.github.mouse0w0.peach.form.Form;
import com.github.mouse0w0.peach.form.FormView;
import com.github.mouse0w0.peach.form.Section;
import com.github.mouse0w0.peach.form.field.*;
import com.github.mouse0w0.peach.javafx.Check;
import com.github.mouse0w0.peach.javafx.util.NotificationLevel;
import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItem;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.model.ModelTemplate;
import com.github.mouse0w0.peach.mcmod.ui.LocalizableConverter;
import com.github.mouse0w0.peach.mcmod.ui.cell.LocalizableCell;
import com.github.mouse0w0.peach.mcmod.ui.cell.LocalizableExCell;
import com.github.mouse0w0.peach.mcmod.ui.form.*;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;

import javax.annotation.Nonnull;

public class ItemEditor extends ElementEditor<MEItem> {

    private final IndexManager indexManager;

    private Form form;

    // Properties
    private TextFieldField identifier;
    private TextFieldField displayName;
    private ComboBoxField<ItemType> itemType;
    private ComboBoxField<ItemGroup> itemGroup;
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
    private ModelField model;
    private ModelTextureField textures;
    private RadioButtonField hasEffect;
    private TextureField armorTexture;

    // Extra
    private SpinnerField<Integer> fuelBurnTime;
    private SpinnerField<Double> hunger;
    private SpinnerField<Double> saturation;
    private RadioButtonField isWolfFood;
    private RadioButtonField alwaysEdible;
    private ItemPickerField foodContainer;

    public ItemEditor(@Nonnull Project project, @Nonnull MEItem element) {
        super(project, element);
        indexManager = IndexManager.getInstance(project);
    }

    @Override
    protected Node getContent() {
        form = new Form();

        Section properties = new Section();
        properties.setText(I18n.translate("item.properties.title"));

        identifier = new TextFieldField();
        identifier.getChecks().add(new Check<>(ModUtils::isValidIdentifier, NotificationLevel.ERROR, I18n.translate("validate.illegalIdentifier")));
        identifier.setText(I18n.translate("item.properties.identifier"));
        identifier.setColSpan(ColSpan.HALF);

        displayName = new TextFieldField();
        displayName.setText(I18n.translate("item.properties.displayName"));
        displayName.setColSpan(ColSpan.HALF);

        itemType = new ComboBoxField<>();
        itemType.setText(I18n.translate("item.properties.itemType"));
        itemType.setCellFactory(LocalizableCell.factory());
        itemType.setButtonCell(new LocalizableCell<>());
        itemType.getItems().setAll(ItemType.values());
        itemType.setColSpan(ColSpan.HALF);
        BooleanBinding isFood = itemType.valueProperty().isEqualTo(ItemType.FOOD);
        BooleanBinding isArmor = itemType.valueProperty().isEqualTo(ItemType.ARMOR);
        BooleanBinding isArmorOrFood = isArmor.or(isFood);

        itemGroup = new ComboBoxField<>();
        itemGroup.setText(I18n.translate("item.properties.itemGroup"));
        itemGroup.setCellFactory(LocalizableExCell.factory());
        itemGroup.setButtonCell(LocalizableExCell.create());
        itemGroup.getItems().setAll(indexManager.getIndex(Indexes.ITEM_GROUPS).values());
        itemGroup.setColSpan(ColSpan.HALF);

        maxStackSize = new SpinnerField<>(1, 64, 64);
        maxStackSize.setText(I18n.translate("item.properties.maxStackSize"));
        maxStackSize.setColSpan(ColSpan.HALF);
        maxStackSize.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            ItemType type = itemType.getValue();
            if (type == ItemType.NORMAL || type == ItemType.FOOD) {
                maxStackSize.setValue(64);
                return false;
            } else {
                maxStackSize.setValue(1);
                return true;
            }
        }, itemType.valueProperty()));

        durability = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        durability.setText(I18n.translate("item.properties.durability"));
        durability.setColSpan(ColSpan.HALF);
        durability.disableProperty().bind(isFood);

        destroySpeed = new SpinnerField<>(0.0, Float.MAX_VALUE, 0.0);
        destroySpeed.setText(I18n.translate("item.properties.destroySpeed"));
        destroySpeed.setColSpan(ColSpan.HALF);
        destroySpeed.disableProperty().bind(isArmorOrFood);

        canDestroyAnyBlock = new RadioButtonField();
        canDestroyAnyBlock.setText(I18n.translate("item.properties.canDestroyAnyBlock"));
        canDestroyAnyBlock.setColSpan(ColSpan.HALF);
        canDestroyAnyBlock.disableProperty().bind(isArmorOrFood);

        toolAttributes = new ToolAttributesField();
        toolAttributes.setText(I18n.translate("item.properties.toolAttributes"));
        toolAttributes.setColSpan(ColSpan.HALF);
        toolAttributes.disableProperty().bind(isArmorOrFood);

        attributeModifiers = new AttributeModifiersField();
        attributeModifiers.setText(I18n.translate("item.properties.attributeModifiers"));

        enchantability = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        enchantability.setText(I18n.translate("item.properties.enchantability"));
        enchantability.setColSpan(ColSpan.HALF);
        enchantability.disableProperty().bind(isFood);

        acceptableEnchantments = new CheckComboBoxField<>();
        acceptableEnchantments.setText(I18n.translate("item.properties.acceptableEnchantments"));
        acceptableEnchantments.setConverter(LocalizableConverter.instance());
        acceptableEnchantments.getItems().setAll(EnchantmentType.values());
        acceptableEnchantments.setColSpan(ColSpan.HALF);
        acceptableEnchantments.disableProperty().bind(isFood);

        equipmentSlot = new ChoiceBoxField<>();
        equipmentSlot.setText(I18n.translate("item.properties.equipmentSlot"));
        equipmentSlot.setConverter(LocalizableConverter.instance());
        equipmentSlot.getItems().setAll(EquipmentSlot.values());
        equipmentSlot.setColSpan(ColSpan.HALF);

        repairItem = new ItemPickerField();
        repairItem.setText(I18n.translate("item.properties.repairItem"));
        repairItem.setFitSize(32, 32);
        repairItem.setColSpan(ColSpan.HALF);
        repairItem.disableProperty().bind(isFood);

        recipeRemain = new ItemPickerField();
        recipeRemain.setText(I18n.translate("item.properties.recipeRemain"));
        recipeRemain.setFitSize(32, 32);
        recipeRemain.setColSpan(ColSpan.HALF);

        useAnimation = new ComboBoxField<>();
        useAnimation.setText(I18n.translate("item.properties.useAnimation"));
        useAnimation.setCellFactory(LocalizableCell.factory());
        useAnimation.setButtonCell(new LocalizableCell<>());
        useAnimation.getItems().setAll(UseAnimation.values());
        useAnimation.setColSpan(ColSpan.HALF);

        useDuration = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        useDuration.setText(I18n.translate("item.properties.useDuration"));
        useDuration.setColSpan(ColSpan.HALF);

        isFood.addListener(observable -> {
            if (isFood.get()) {
                useAnimation.setValue(UseAnimation.EAT);
                useDuration.setValue(32);
            } else {
                useAnimation.setValue(UseAnimation.NONE);
                useDuration.setValue(0);
            }
        });

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

        Section appearance = new Section();
        appearance.setText(I18n.translate("item.appearance.title"));

        model = new ModelField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.MODELS),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_MODELS), ".json"));
        model.setText(I18n.translate("item.appearance.model"));
        model.getItems().addAll(ModelManager.getInstance().getModelTemplatesByGroup("item"));

        textures = new ModelTextureField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_TEXTURES), ".png"));
        textures.setText(I18n.translate("item.appearance.texture"));
        model.valueProperty().addListener(observable -> {
            ModelTemplate template = ModelManager.getInstance().getModelTemplate(model.getValue());
            textures.setTextureKeys(template != null ? template.getTextures() : null);
        });

        hasEffect = new RadioButtonField();
        hasEffect.setText(I18n.translate("item.appearance.hasEffect"));
        hasEffect.setColSpan(ColSpan.HALF);

        armorTexture = new TextureField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ARMOR_TEXTURES)));
        armorTexture.setFitSize(128, 64);
        armorTexture.setText(I18n.translate("item.appearance.armorTexture"));
        armorTexture.visibleProperty().bind(isArmor);

        appearance.getElements().addAll(model, textures, hasEffect, armorTexture);

        Section extra = new Section();
        extra.setText(I18n.translate("item.extra.title"));

        fuelBurnTime = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        fuelBurnTime.setText(I18n.translate("item.fuel.fuelBurnTime"));
        fuelBurnTime.setColSpan(ColSpan.HALF);

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

        foodContainer = new ItemPickerField();
        foodContainer.setText(I18n.translate("item.food.foodContainer"));
        foodContainer.setFitSize(32, 32);
        foodContainer.setColSpan(ColSpan.HALF);
        foodContainer.visibleProperty().bind(isFood);

        extra.getElements().addAll(fuelBurnTime, hunger, saturation, isWolfFood, alwaysEdible, foodContainer);

        form.getGroups().addAll(properties, appearance, extra);

        return new FormView(form);
    }

    @Override
    protected void initialize(MEItem item) {
        identifier.setValue(item.getIdentifier());
        displayName.setValue(item.getDisplayName());
        itemType.setValue(item.getItemType());
        itemGroup.setValue(item.getItemGroup());
        maxStackSize.setValue(item.getMaxStackSize());
        durability.setValue(item.getDurability());
        destroySpeed.setValue(item.getDestroySpeed());
        canDestroyAnyBlock.setValue(item.isCanDestroyAnyBlock());
        toolAttributes.setValue(item.getToolAttributes());
        attributeModifiers.setValue(item.getAttributeModifiers());
        enchantability.setValue(item.getEnchantability());
        acceptableEnchantments.setValue(item.getAcceptableEnchantments());
        equipmentSlot.setValue(item.getEquipmentSlot());
        repairItem.setValue(item.getRepairItem());
        recipeRemain.setValue(item.getRecipeRemain());
        useAnimation.setValue(item.getUseAnimation());
        useDuration.setValue(item.getUseDuration());
        information.setValue(item.getInformation());

        model.setValue(item.getModel());
        textures.setTextures(item.getTextures());
        hasEffect.setValue(item.isHasEffect());
        armorTexture.setTexture(item.getArmorTexture());

        fuelBurnTime.setValue(item.getFuelBurnTime());
        hunger.setValue(item.getHunger());
        saturation.setValue(item.getSaturation());
        isWolfFood.setValue(item.isWolfFood());
        alwaysEdible.setValue(item.isAlwaysEdible());
        foodContainer.setValue(item.getFoodContainer());
    }

    @Override
    protected void updateDataModel(MEItem item) {
        item.setIdentifier(identifier.getValue().trim());
        item.setDisplayName(displayName.getValue());
        item.setItemType(itemType.getValue());
        item.setItemGroup(itemGroup.getValue());
        item.setMaxStackSize(maxStackSize.getValue());
        item.setDurability(durability.getValue());
        item.setDestroySpeed(destroySpeed.getValue());
        item.setCanDestroyAnyBlock(canDestroyAnyBlock.getValue());
        item.setToolAttributes(toolAttributes.getValue());
        item.setAttributeModifiers(attributeModifiers.getValue());
        item.setEnchantability(enchantability.getValue());
        item.setAcceptableEnchantments(acceptableEnchantments.getValue(EnchantmentType[]::new));
        item.setEquipmentSlot(equipmentSlot.getValue());
        item.setRepairItem(repairItem.getValue());
        item.setRecipeRemain(recipeRemain.getValue());
        item.setUseAnimation(useAnimation.getValue());
        item.setUseDuration(useDuration.getValue());
        item.setInformation(information.getValue());

        item.setModel(model.getValue());
        item.setTextures(textures.getTextures());
        item.setHasEffect(hasEffect.getValue());
        item.setArmorTexture(armorTexture.getTexture());

        item.setFuelBurnTime(fuelBurnTime.getValue());
        item.setHunger(hunger.getValue());
        item.setSaturation(saturation.getValue());
        item.setWolfFood(isWolfFood.getValue());
        item.setAlwaysEdible(alwaysEdible.getValue());
        item.setFoodContainer(foodContainer.getValue());
    }

    @Override
    protected boolean validate() {
        return form.validate();
    }
}
