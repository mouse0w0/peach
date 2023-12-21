package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexTypes;
import com.github.mouse0w0.peach.mcmod.ui.LocalizableConverter;
import com.github.mouse0w0.peach.mcmod.ui.cell.AttributeModifierCell;
import com.github.mouse0w0.peach.mcmod.ui.cell.LocalizableCell;
import com.github.mouse0w0.peach.mcmod.ui.cell.LocalizableWithItemIconCell;
import com.github.mouse0w0.peach.mcmod.ui.cell.ToolAttributeCell;
import com.github.mouse0w0.peach.mcmod.ui.form.ItemPickerField;
import com.github.mouse0w0.peach.mcmod.ui.form.ModelField;
import com.github.mouse0w0.peach.mcmod.ui.form.ModelTextureField;
import com.github.mouse0w0.peach.mcmod.ui.form.TextureField;
import com.github.mouse0w0.peach.mcmod.util.IdentifierUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.form.ColSpan;
import com.github.mouse0w0.peach.ui.form.Form;
import com.github.mouse0w0.peach.ui.form.FormView;
import com.github.mouse0w0.peach.ui.form.Section;
import com.github.mouse0w0.peach.ui.form.field.*;
import com.github.mouse0w0.peach.ui.util.Check;
import com.github.mouse0w0.peach.util.StringUtils;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

public class ItemEditor extends ElementEditor<ItemElement> {
    private Form form;

    // Properties
    private TextFieldField identifier;
    private TextFieldField displayName;
    private ComboBoxField<ItemType> type;
    private ComboBoxField<String> itemGroup;
    private IntegerField maxStackSize;
    private IntegerField durability;
    private ChoiceBoxField<EquipmentSlot> equipmentSlot;
    private TagViewField<ToolAttribute> toolAttributes;
    private DoubleField destroySpeed;
    private RadioButtonField canDestroyAnyBlock;
    private DoubleField attackDamage;
    private DoubleField attackSpeed;
    private IntegerField enchantability;
    private CheckComboBoxField<EnchantmentType> acceptableEnchantments;
    private TagViewField<AttributeModifier> attributeModifiers;
    private ItemPickerField repairItem;
    private ItemPickerField recipeRemain;
    private ComboBoxField<UseAnimation> useAnimation;
    private IntegerField useDuration;
    private IntegerField hitEntityLoss;
    private IntegerField destroyBlockLoss;
    private TextAreaField information;

    // Appearance
    private ModelField model;
    private ModelTextureField textures;
    private RadioButtonField hasEffect;
    private TextureField armorTexture;

    // Extra
    private IntegerField fuelBurnTime;
    private ComboBoxField<String> equipSound;
    private IntegerField hunger;
    private DoubleField saturation;
    private RadioButtonField isWolfFood;
    private RadioButtonField alwaysEdible;
    private ItemPickerField foodContainer;

    public ItemEditor(@NotNull Project project, @NotNull ItemElement element) {
        super(project, element);
    }

    @Override
    protected Node getContent() {
        var indexManager = IndexManager.getInstance(getProject());

        form = new Form();

        Section properties = new Section();
        properties.setText(AppL10n.localize("item.properties.title"));

        identifier = new TextFieldField();
        identifier.getChecks().add(Check.of(AppL10n.localize("validate.invalidIdentifier"), IdentifierUtils::validateIdentifier));
        identifier.setLabel(AppL10n.localize("item.properties.identifier"));
        identifier.setColSpan(ColSpan.HALF);

        displayName = new TextFieldField();
        displayName.setLabel(AppL10n.localize("item.properties.displayName"));
        displayName.setColSpan(ColSpan.HALF);

        type = new ComboBoxField<>();
        type.setLabel(AppL10n.localize("item.properties.type"));
        type.setCellFactory(LocalizableCell.factory());
        type.setButtonCell(new LocalizableCell<>());
        type.getItems().setAll(ItemType.VALUES);
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
        itemGroup.setLabel(AppL10n.localize("item.properties.itemGroup"));
        var itemGroupMap = indexManager.getIndex(IndexTypes.ITEM_GROUPS);
        itemGroup.setCellFactory(LocalizableWithItemIconCell.factory(itemGroupMap));
        itemGroup.setButtonCell(LocalizableWithItemIconCell.create(itemGroupMap));
        itemGroup.getItems().addAll(itemGroupMap.keySet());
        itemGroup.setColSpan(ColSpan.HALF);

        maxStackSize = new IntegerField(1, 64, 64);
        maxStackSize.setLabel(AppL10n.localize("item.properties.maxStackSize"));
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

        durability = new IntegerField(0, Integer.MAX_VALUE, 0);
        durability.setLabel(AppL10n.localize("item.properties.durability"));
        durability.setColSpan(ColSpan.HALF);
        durability.disableProperty().bind(isFood);

        equipmentSlot = new ChoiceBoxField<>();
        equipmentSlot.setLabel(AppL10n.localize("item.properties.equipmentSlot"));
        equipmentSlot.setConverter(LocalizableConverter.instance());
        equipmentSlot.getItems().addAll(EquipmentSlot.ALL_SLOTS);
        equipmentSlot.setColSpan(ColSpan.HALF);
        equipmentSlot.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            final ItemType type = this.type.getValue();
            if (type == ItemType.FOOD) {
                equipmentSlot.getItems().setAll(EquipmentSlot.ALL_SLOTS);
                equipmentSlot.setValue(EquipmentSlot.NONE);
                return true;
            } else if (type == ItemType.NORMAL) {
                equipmentSlot.getItems().setAll(EquipmentSlot.ALL_SLOTS);
                equipmentSlot.setValue(EquipmentSlot.NONE);
            } else if (type == ItemType.SWORD || type == ItemType.TOOL) {
                equipmentSlot.getItems().setAll(EquipmentSlot.HAND_SLOTS);
                equipmentSlot.setValue(EquipmentSlot.MAINHAND);
            } else if (type == ItemType.ARMOR) {
                equipmentSlot.getItems().setAll(EquipmentSlot.ARMOR_SLOTS);
                equipmentSlot.setValue(EquipmentSlot.HEAD);
            }
            return false;
        }, type.valueProperty()));

        toolAttributes = new TagViewField<>();
        toolAttributes.setLabel(AppL10n.localize("item.properties.toolAttributes"));
        toolAttributes.setColSpan(ColSpan.HALF);
        toolAttributes.disableProperty().bind(isArmorOrFood);
        toolAttributes.setItemFactory(() -> new ToolAttribute("axe", 0));
        toolAttributes.setCellFactory(view -> new ToolAttributeCell());

        destroySpeed = new DoubleField(0.0, Double.MAX_VALUE, 0D);
        destroySpeed.setLabel(AppL10n.localize("item.properties.destroySpeed"));
        destroySpeed.setColSpan(ColSpan.HALF);
        destroySpeed.disableProperty().bind(isArmorOrFood);

        canDestroyAnyBlock = new RadioButtonField();
        canDestroyAnyBlock.setLabel(AppL10n.localize("item.properties.canDestroyAnyBlock"));
        canDestroyAnyBlock.setColSpan(ColSpan.HALF);
        canDestroyAnyBlock.disableProperty().bind(isArmorOrFood);

        attackDamage = new DoubleField(-Double.MAX_VALUE, Double.MAX_VALUE, 1D);
        attackDamage.setLabel(AppL10n.localize("item.properties.attackDamage"));
        attackDamage.setColSpan(ColSpan.HALF);

        attackSpeed = new DoubleField(-Double.MAX_VALUE, Double.MAX_VALUE, 4D);
        attackSpeed.setLabel(AppL10n.localize("item.properties.attackSpeed"));
        attackSpeed.setColSpan(ColSpan.HALF);
        type.valueProperty().addListener(observable -> {
            ItemType type = this.type.getValue();
            if (type == ItemType.SWORD) {
                attackSpeed.setValue(1.6D);
            } else if (type == ItemType.TOOL) {
                attackSpeed.setValue(1D);
            } else {
                attackSpeed.setValue(4D);
            }
        });

        enchantability = new IntegerField(0, Integer.MAX_VALUE, 0);
        enchantability.setLabel(AppL10n.localize("item.properties.enchantability"));
        enchantability.setColSpan(ColSpan.HALF);
        enchantability.disableProperty().bind(isFood);

        acceptableEnchantments = new CheckComboBoxField<>();
        acceptableEnchantments.setLabel(AppL10n.localize("item.properties.acceptableEnchantments"));
        acceptableEnchantments.setConverter(LocalizableConverter.instance());
        acceptableEnchantments.getItems().setAll(EnchantmentType.VALUES);
        acceptableEnchantments.setColSpan(ColSpan.HALF);
        acceptableEnchantments.disableProperty().bind(isFood);

        attributeModifiers = new TagViewField<>();
        attributeModifiers.setLabel(AppL10n.localize("item.properties.attributeModifiers"));
        attributeModifiers.disableProperty().bind(equipmentSlot.valueProperty().isEqualTo(EquipmentSlot.NONE));
        attributeModifiers.setItemFactory(() -> new AttributeModifier(Attribute.MAX_HEALTH, 0, AttributeModifier.Operation.ADD));
        attributeModifiers.setCellFactory(view -> new AttributeModifierCell());

        repairItem = new ItemPickerField();
        repairItem.setSize(32);
        repairItem.setLabel(AppL10n.localize("item.properties.repairItem"));
        repairItem.setColSpan(ColSpan.HALF);
        repairItem.disableProperty().bind(isFood);

        recipeRemain = new ItemPickerField();
        recipeRemain.setSize(32);
        recipeRemain.setLabel(AppL10n.localize("item.properties.recipeRemain"));
        recipeRemain.setColSpan(ColSpan.HALF);

        useAnimation = new ComboBoxField<>();
        useAnimation.setLabel(AppL10n.localize("item.properties.useAnimation"));
        useAnimation.setCellFactory(LocalizableCell.factory());
        useAnimation.setButtonCell(new LocalizableCell<>());
        useAnimation.getItems().setAll(UseAnimation.VALUES);
        useAnimation.setColSpan(ColSpan.HALF);

        useDuration = new IntegerField(0, Integer.MAX_VALUE, 0);
        useDuration.setLabel(AppL10n.localize("item.properties.useDuration"));
        useDuration.setColSpan(ColSpan.HALF);

        hitEntityLoss = new IntegerField(0, Integer.MAX_VALUE, 0);
        hitEntityLoss.setLabel(AppL10n.localize("item.properties.hitEntityLoss"));
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

        destroyBlockLoss = new IntegerField(0, Integer.MAX_VALUE, 0);
        destroyBlockLoss.setLabel(AppL10n.localize("item.properties.destroyBlockLoss"));
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
        information.setLabel(AppL10n.localize("item.properties.information"));

        properties.getElements().addAll(
                identifier, displayName,
                type, itemGroup,
                maxStackSize, durability,
                equipmentSlot, toolAttributes,
                destroySpeed, canDestroyAnyBlock,
                attackDamage, attackSpeed,
                enchantability, acceptableEnchantments,
                attributeModifiers,
                repairItem, recipeRemain,
                useAnimation, useDuration,
                hitEntityLoss, destroyBlockLoss,
                information);

        Section appearance = new Section();
        appearance.setText(AppL10n.localize("item.appearance.title"));

        model = new ModelField(getProject(), new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.MODELS),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_MODELS), ".json"));
        model.setText(AppL10n.localize("item.appearance.model"));
        model.setBlockstate("item");

        textures = new ModelTextureField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_TEXTURES), ".png"));
        textures.setLabel(AppL10n.localize("item.appearance.texture"));
        model.getTextures().addListener((InvalidationListener) observable -> textures.setTextureKeys(model.getTextures()));

        hasEffect = new RadioButtonField();
        hasEffect.setLabel(AppL10n.localize("item.appearance.hasEffect"));
        hasEffect.setColSpan(ColSpan.HALF);

        armorTexture = new TextureField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ARMOR_TEXTURES)));
        armorTexture.setFitSize(128, 64);
        armorTexture.setLabel(AppL10n.localize("item.appearance.armorTexture"));
        armorTexture.visibleProperty().bind(isArmor);

        appearance.getElements().addAll(model, textures, hasEffect, armorTexture);

        Section extra = new Section();
        extra.setText(AppL10n.localize("item.extra.title"));

        fuelBurnTime = new IntegerField(0, Integer.MAX_VALUE, 0);
        fuelBurnTime.setLabel(AppL10n.localize("item.fuel.fuelBurnTime"));
        fuelBurnTime.setColSpan(ColSpan.HALF);

        equipSound = new ComboBoxField<>();
        equipSound.setLabel(AppL10n.localize("item.armor.equipSound"));
        equipSound.setColSpan(ColSpan.HALF);
        var soundEventMap = indexManager.getIndex(IndexTypes.SOUND_EVENTS);
        equipSound.setCellFactory(LocalizableWithItemIconCell.factory(soundEventMap));
        equipSound.setButtonCell(LocalizableWithItemIconCell.create(soundEventMap));
        equipSound.getItems().addAll(soundEventMap.keySet());
        equipSound.disableProperty().bind(isNotArmor);

        hunger = new IntegerField(0, Integer.MAX_VALUE, 0);
        hunger.setLabel(AppL10n.localize("item.food.hunger"));
        hunger.setColSpan(ColSpan.HALF);
        hunger.disableProperty().bind(isNotFood);

        saturation = new DoubleField(0.0, Double.MAX_VALUE, 0.6);
        saturation.setLabel(AppL10n.localize("item.food.saturation"));
        saturation.setColSpan(ColSpan.HALF);
        saturation.disableProperty().bind(isNotFood);

        isWolfFood = new RadioButtonField();
        isWolfFood.setLabel(AppL10n.localize("item.food.isWolfFood"));
        isWolfFood.setColSpan(ColSpan.HALF);
        isWolfFood.disableProperty().bind(isNotFood);

        alwaysEdible = new RadioButtonField();
        alwaysEdible.setLabel(AppL10n.localize("item.food.alwaysEdible"));
        alwaysEdible.setColSpan(ColSpan.HALF);
        alwaysEdible.disableProperty().bind(isNotFood);

        foodContainer = new ItemPickerField();
        foodContainer.setSize(32);
        foodContainer.setLabel(AppL10n.localize("item.food.foodContainer"));
        foodContainer.setColSpan(ColSpan.HALF);
        foodContainer.disableProperty().bind(isNotFood);

        extra.getElements().addAll(fuelBurnTime, equipSound, hunger, saturation, isWolfFood, alwaysEdible, foodContainer);

        form.getGroups().addAll(properties, appearance, extra);

        return new FormView(form);
    }

    @Override
    protected void initialize(ItemElement item) {
        identifier.setValue(item.getIdentifier());
        displayName.setValue(item.getDisplayName());
        type.setValue(item.getType());
        itemGroup.setValue(item.getItemGroup());
        maxStackSize.setValue(item.getMaxStackSize());
        durability.setValue(item.getDurability());
        equipmentSlot.setValue(item.getEquipmentSlot());
        toolAttributes.setValues(item.getToolAttributes());
        destroySpeed.setValue(item.getDestroySpeed());
        canDestroyAnyBlock.set(item.isCanDestroyAnyBlock());
        attackDamage.setValue(item.getAttackDamage());
        attackSpeed.setValue(item.getAttackSpeed());
        attributeModifiers.setValues(item.getAttributeModifiers());
        enchantability.setValue(item.getEnchantability());
        acceptableEnchantments.setValues(item.getAcceptableEnchantments());
        repairItem.setValue(item.getRepairItem());
        recipeRemain.setValue(item.getRecipeRemain());
        useAnimation.setValue(item.getUseAnimation());
        useDuration.setValue(item.getUseDuration());
        hitEntityLoss.setValue(item.getHitEntityLoss());
        destroyBlockLoss.setValue(item.getDestroyBlockLoss());
        information.setValue(StringUtils.join(item.getInformation(), '\n'));

        model.setModel(item.getModel());
        model.setCustomModels(item.getCustomModels());
        textures.setTextures(item.getTextures());
        hasEffect.set(item.isHasEffect());
        armorTexture.setTexture(item.getArmorTexture());

        fuelBurnTime.setValue(item.getFuelBurnTime());
        equipSound.setValue(item.getEquipSound());
        hunger.setValue(item.getHunger());
        saturation.setValue(item.getSaturation());
        isWolfFood.set(item.isWolfFood());
        alwaysEdible.set(item.isAlwaysEdible());
        foodContainer.setValue(item.getFoodContainer());
    }

    @Override
    protected void updateDataModel(ItemElement item) {
        item.setIdentifier(identifier.getValue().trim());
        item.setDisplayName(displayName.getValue().trim());
        item.setType(type.getValue());
        item.setItemGroup(itemGroup.getValue());
        item.setMaxStackSize(maxStackSize.getValue());
        item.setDurability(durability.getValue());
        item.setEquipmentSlot(equipmentSlot.getValue());
        item.setToolAttributes(toolAttributes.getValues().toArray(ToolAttribute.EMPTY_ARRAY));
        item.setDestroySpeed(destroySpeed.getValue());
        item.setCanDestroyAnyBlock(canDestroyAnyBlock.get());
        item.setAttackDamage(attackDamage.getValue());
        item.setAttackSpeed(attackSpeed.getValue());
        item.setAttributeModifiers(attributeModifiers.getValues().toArray(AttributeModifier.EMPTY_ARRAY));
        item.setEnchantability(enchantability.getValue());
        item.setAcceptableEnchantments(acceptableEnchantments.getValues().toArray(EnchantmentType.EMPTY_ARRAY));
        item.setRepairItem(repairItem.getValue());
        item.setRecipeRemain(recipeRemain.getValue());
        item.setUseAnimation(useAnimation.getValue());
        item.setUseDuration(useDuration.getValue());
        item.setHitEntityLoss(hitEntityLoss.getValue());
        item.setDestroyBlockLoss(destroyBlockLoss.getValue());
        item.setInformation(StringUtils.splitByLineSeparator(information.getValue()));

        item.setModel(model.getModel());
        item.setCustomModels(model.getCustomModels());
        item.setTextures(textures.getTextures());
        item.setHasEffect(hasEffect.get());
        item.setArmorTexture(armorTexture.getTexture());

        item.setFuelBurnTime(fuelBurnTime.getValue());
        item.setEquipSound(equipSound.getValue());
        item.setHunger(hunger.getValue());
        item.setSaturation(saturation.getValue());
        item.setWolfFood(isWolfFood.get());
        item.setAlwaysEdible(alwaysEdible.get());
        item.setFoodContainer(foodContainer.getValue());
    }

    @Override
    protected boolean validate() {
        return form.validate();
    }
}
