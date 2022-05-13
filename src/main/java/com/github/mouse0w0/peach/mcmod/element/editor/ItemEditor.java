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
import com.github.mouse0w0.peach.mcmod.ui.LocalizableConverter;
import com.github.mouse0w0.peach.mcmod.ui.cell.LocalizableCell;
import com.github.mouse0w0.peach.mcmod.ui.cell.LocalizableExCell;
import com.github.mouse0w0.peach.mcmod.ui.form.*;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import javafx.beans.InvalidationListener;
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
    private ComboBoxField<ItemType> type;
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
    private SpinnerField<Integer> hitEntityLoss;
    private SpinnerField<Integer> destroyBlockLoss;
    private TextAreaField information;

    // Appearance
    private ModelField model;
    private ModelTextureField textures;
    private RadioButtonField hasEffect;
    private TextureField armorTexture;

    // Extra
    private SpinnerField<Integer> fuelBurnTime;
    private ChoiceBoxField<SoundEvent> armorSound;
    private SpinnerField<Integer> hunger;
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

        type = new ComboBoxField<>();
        type.setText(I18n.translate("item.properties.type"));
        type.setCellFactory(LocalizableCell.factory());
        type.setButtonCell(new LocalizableCell<>());
        type.getItems().setAll(ItemType.values());
        type.setColSpan(ColSpan.HALF);
        BooleanBinding isFood = type.valueProperty().isEqualTo(ItemType.FOOD);
        BooleanBinding isNotFood = type.valueProperty().isNotEqualTo(ItemType.FOOD);
        BooleanBinding isArmor = type.valueProperty().isEqualTo(ItemType.ARMOR);
        BooleanBinding isNotArmor = type.valueProperty().isNotEqualTo(ItemType.ARMOR);
        BooleanBinding isArmorOrFood = isArmor.or(isFood);

        isFood.addListener(observable -> {
            if (isFood.get()) {
                durability.setValue(0);
                useAnimation.setValue(UseAnimation.EAT);
                useDuration.setValue(32);
            } else {
                useAnimation.setValue(UseAnimation.NONE);
                useDuration.setValue(0);
            }
        });

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
            ItemType type = this.type.getValue();
            if (type == ItemType.NORMAL || type == ItemType.FOOD) {
                maxStackSize.setValue(64);
                return false;
            } else {
                maxStackSize.setValue(1);
                return true;
            }
        }, type.valueProperty()));

        durability = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        durability.setText(I18n.translate("item.properties.durability"));
        durability.setColSpan(ColSpan.HALF);
        durability.disableProperty().bind(isFood);

        destroySpeed = new SpinnerField<>(0.0, Double.MAX_VALUE, 0.0);
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
        equipmentSlot.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            ItemType type = this.type.getValue();
            if (type == ItemType.FOOD) {
                equipmentSlot.getItems().setAll(EquipmentSlot.HAND_SLOTS);
                equipmentSlot.setValue(EquipmentSlot.MAINHAND);
                return true;
            }

            if (type == ItemType.NORMAL) {
                equipmentSlot.getItems().setAll(EquipmentSlot.values());
                equipmentSlot.setValue(EquipmentSlot.MAINHAND);
            } else if (type == ItemType.SWORD || type == ItemType.TOOL) {
                equipmentSlot.getItems().setAll(EquipmentSlot.HAND_SLOTS);
                equipmentSlot.setValue(EquipmentSlot.MAINHAND);
            } else if (type == ItemType.ARMOR) {
                equipmentSlot.getItems().setAll(EquipmentSlot.ARMOR_SLOTS);
                equipmentSlot.setValue(EquipmentSlot.HEAD);
            }
            return false;
        }, type.valueProperty()));

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

        hitEntityLoss = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        hitEntityLoss.setText(I18n.translate("item.properties.hitEntityLoss"));
        hitEntityLoss.setColSpan(ColSpan.HALF);
        hitEntityLoss.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            if (durability.getValue() == 0) return true;
            ItemType type = this.type.getValue();
            if (type == ItemType.NORMAL) {
                hitEntityLoss.setValue(0);
                return false;
            } else if (type == ItemType.TOOL) {
                hitEntityLoss.setValue(2);
                return false;
            } else if (type == ItemType.SWORD) {
                hitEntityLoss.setValue(1);
                return false;
            } else {
                hitEntityLoss.setValue(0);
                return true;
            }
        }, type.valueProperty(), durability.valueProperty()));

        destroyBlockLoss = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        destroyBlockLoss.setText(I18n.translate("item.properties.destroyBlockLoss"));
        destroyBlockLoss.setColSpan(ColSpan.HALF);
        destroyBlockLoss.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            if (durability.getValue() == 0) return true;
            ItemType type = this.type.getValue();
            if (type == ItemType.NORMAL) {
                destroyBlockLoss.setValue(0);
                return false;
            } else if (type == ItemType.TOOL) {
                destroyBlockLoss.setValue(1);
                return false;
            } else if (type == ItemType.SWORD) {
                destroyBlockLoss.setValue(2);
                return false;
            } else {
                destroyBlockLoss.setValue(0);
                return true;
            }
        }, type.valueProperty(), durability.valueProperty()));

        information = new TextAreaField();
        information.setText(I18n.translate("item.properties.information"));

        properties.getElements().addAll(
                identifier, displayName,
                type, itemGroup,
                maxStackSize, durability,
                equipmentSlot, toolAttributes,
                destroySpeed, canDestroyAnyBlock,
                enchantability, acceptableEnchantments,
                attributeModifiers,
                repairItem, recipeRemain,
                useAnimation, useDuration,
                hitEntityLoss, destroyBlockLoss,
                information);

        Section appearance = new Section();
        appearance.setText(I18n.translate("item.appearance.title"));

        model = new ModelField(getProject(), new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.MODELS),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_MODELS), ".json"));
        model.setText(I18n.translate("item.appearance.model"));
        model.setBlockstate("item");

        textures = new ModelTextureField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_TEXTURES), ".png"));
        textures.setText(I18n.translate("item.appearance.texture"));
        model.getTextures().addListener((InvalidationListener) observable -> textures.setTextureKeys(model.getTextures()));

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

        armorSound = new ChoiceBoxField<>();
        armorSound.setText(I18n.translate("item.armor.armorSound"));
        armorSound.setColSpan(ColSpan.HALF);
        armorSound.setConverter(LocalizableConverter.instance());
        armorSound.getItems().addAll(IndexManager.getInstance(getProject()).getIndex(Indexes.SOUND_EVENTS).values());
        armorSound.disableProperty().bind(isNotArmor);

        hunger = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        hunger.setText(I18n.translate("item.food.hunger"));
        hunger.setColSpan(ColSpan.HALF);
        hunger.disableProperty().bind(isNotFood);

        saturation = new SpinnerField<>(0.0, Double.MAX_VALUE, 0.6);
        saturation.setText(I18n.translate("item.food.saturation"));
        saturation.setColSpan(ColSpan.HALF);
        saturation.disableProperty().bind(isNotFood);

        isWolfFood = new RadioButtonField();
        isWolfFood.setText(I18n.translate("item.food.isWolfFood"));
        isWolfFood.setColSpan(ColSpan.HALF);
        isWolfFood.disableProperty().bind(isNotFood);

        alwaysEdible = new RadioButtonField();
        alwaysEdible.setText(I18n.translate("item.food.alwaysEdible"));
        alwaysEdible.setColSpan(ColSpan.HALF);
        alwaysEdible.disableProperty().bind(isNotFood);

        foodContainer = new ItemPickerField();
        foodContainer.setText(I18n.translate("item.food.foodContainer"));
        foodContainer.setFitSize(32, 32);
        foodContainer.setColSpan(ColSpan.HALF);
        foodContainer.disableProperty().bind(isNotFood);

        extra.getElements().addAll(fuelBurnTime, armorSound, hunger, saturation, isWolfFood, alwaysEdible, foodContainer);

        form.getGroups().addAll(properties, appearance, extra);

        return new FormView(form);
    }

    @Override
    protected void initialize(MEItem item) {
        identifier.setValue(item.getIdentifier());
        displayName.setValue(item.getDisplayName());
        type.setValue(item.getType());
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
        hitEntityLoss.setValue(item.getHitEntityLoss());
        destroyBlockLoss.setValue(item.getDestroyBlockLoss());
        information.setValue(item.getInformation());

        model.setModelPrototype(item.getModelPrototype());
        model.setModels(item.getModels());
        textures.setTextures(item.getTextures());
        hasEffect.setValue(item.isHasEffect());
        armorTexture.setTexture(item.getArmorTexture());

        fuelBurnTime.setValue(item.getFuelBurnTime());
        armorSound.setValue(item.getArmorSound());
        hunger.setValue(item.getHunger());
        saturation.setValue(item.getSaturation());
        isWolfFood.setValue(item.isWolfFood());
        alwaysEdible.setValue(item.isAlwaysEdible());
        foodContainer.setValue(item.getFoodContainer());
    }

    @Override
    protected void updateDataModel(MEItem item) {
        item.setIdentifier(identifier.getValue().trim());
        item.setDisplayName(displayName.getValue().trim());
        item.setType(type.getValue());
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
        item.setHitEntityLoss(hitEntityLoss.getValue());
        item.setDestroyBlockLoss(destroyBlockLoss.getValue());
        item.setInformation(information.getValue());

        item.setModelPrototype(model.getModelPrototype());
        item.setModels(model.getModels());
        item.setTextures(textures.getTextures());
        item.setHasEffect(hasEffect.getValue());
        item.setArmorTexture(armorTexture.getTexture());

        item.setFuelBurnTime(fuelBurnTime.getValue());
        item.setArmorSound(armorSound.getValue());
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
