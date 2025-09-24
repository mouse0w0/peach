package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.mcmod.AttributeModifier;
import com.github.mouse0w0.peach.mcmod.EquipmentSlot;
import com.github.mouse0w0.peach.mcmod.ItemType;
import com.github.mouse0w0.peach.mcmod.ToolAttribute;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.index.IndexKeys;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.ui.GameDataConverter;
import com.github.mouse0w0.peach.mcmod.ui.LocalizableConverter;
import com.github.mouse0w0.peach.mcmod.ui.cell.AttributeModifierCell;
import com.github.mouse0w0.peach.mcmod.ui.cell.IconicDataCell;
import com.github.mouse0w0.peach.mcmod.ui.cell.LocalizableCell;
import com.github.mouse0w0.peach.mcmod.ui.cell.ToolAttributeCell;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ModelPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.TexturePicker;
import com.github.mouse0w0.peach.mcmod.util.IdentifierUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.control.CheckComboBox;
import com.github.mouse0w0.peach.ui.control.DoubleSpinner;
import com.github.mouse0w0.peach.ui.control.IntegerSpinner;
import com.github.mouse0w0.peach.ui.control.TagView;
import com.github.mouse0w0.peach.ui.util.ExtensionFilters;
import com.github.mouse0w0.peach.ui.util.Validator;
import com.github.mouse0w0.peach.util.ArrayUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

import static com.github.mouse0w0.peach.l10n.AppL10n.localize;
import static com.github.mouse0w0.peach.ui.layout.Form.form;
import static com.github.mouse0w0.peach.ui.layout.FormItem.half;
import static com.github.mouse0w0.peach.ui.layout.FormItem.one;
import static com.github.mouse0w0.peach.ui.layout.LayoutUtils.scrollVBox;
import static com.github.mouse0w0.peach.ui.layout.LayoutUtils.titled;

public class ItemEditor extends ElementEditor<ItemElement> {
    // Properties
    private TextField identifier;
    private TextField displayName;
    private ComboBox<ItemType> type;
    private ComboBox<String> itemGroup;
    private IntegerSpinner maxStackSize;
    private IntegerSpinner durability;
    private ComboBox<EquipmentSlot> equipmentSlot;
    private TagView<ToolAttribute> toolAttributes;
    private DoubleSpinner destroySpeed;
    private RadioButton canDestroyAnyBlock;
    private DoubleSpinner attackDamage;
    private DoubleSpinner attackSpeed;
    private IntegerSpinner enchantability;
    private CheckComboBox<String> acceptableEnchantments;
    private TagView<AttributeModifier> attributeModifiers;
    private ItemPicker repairItem;
    private ItemPicker recipeRemain;
    private ComboBox<String> useAnimation;
    private IntegerSpinner useDuration;
    private IntegerSpinner hitEntityLoss;
    private IntegerSpinner destroyBlockLoss;
    private TextArea information;

    // Appearance
    private ModelPicker model;
    private RadioButton hasEffect;
    private TexturePicker armorTexture;

    // Extra
    private IntegerSpinner fuelBurnTime;
    private ComboBox<String> equipSound;
    private IntegerSpinner hunger;
    private DoubleSpinner saturation;
    private RadioButton isWolfFood;
    private RadioButton alwaysEdible;
    private ItemPicker foodContainer;

    public ItemEditor(@NotNull Project project, @NotNull ItemElement element) {
        super(project, element);
    }

    @Override
    protected Node getContent() {
        var indexManager = IndexManager.getInstance(getProject());

        identifier = new TextField();
        Validator.of(identifier, localize("validate.invalidIdentifier"), IdentifierUtils::validateIdentifier);

        displayName = new TextField();

        type = new ComboBox<>();
        type.setCellFactory(LocalizableCell.factory());
        type.setButtonCell(new LocalizableCell<>());
        type.getItems().setAll(ItemType.VALUES);

        BooleanBinding isFood = type.valueProperty().isEqualTo(ItemType.FOOD);
        BooleanBinding isNotFood = isFood.not();
        BooleanBinding isArmor = type.valueProperty().isEqualTo(ItemType.ARMOR);
        BooleanBinding isNotArmor = isArmor.not();
        BooleanBinding isArmorOrFood = isArmor.or(isFood);

        isFood.addListener(observable -> {
            if (isFood.get()) {
                durability.setValue(0);
                useAnimation.setValue("EAT");
                useDuration.setValue(32);
            } else {
                useAnimation.setValue("NONE");
                useDuration.setValue(0);
            }
        });

        var itemGroupMap = indexManager.getIndex(IndexKeys.ITEM_GROUP);
        itemGroup = new ComboBox<>();
        itemGroup.setCellFactory(IconicDataCell.factory(getProject(), itemGroupMap));
        itemGroup.setButtonCell(IconicDataCell.create(getProject(), itemGroupMap));
        itemGroup.setItems(itemGroupMap.keyList());

        maxStackSize = new IntegerSpinner(1, 64, 64);
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

        durability = new IntegerSpinner(0, Integer.MAX_VALUE, 0);
        durability.disableProperty().bind(isFood);

        equipmentSlot = new ComboBox<>();
        equipmentSlot.setConverter(LocalizableConverter.instance());
        equipmentSlot.getItems().addAll(EquipmentSlot.ALL_SLOTS);
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

        var toolTypeIndex = indexManager.getIndex(IndexKeys.TOOL_TYPE);
        toolAttributes = new TagView<>();
        toolAttributes.disableProperty().bind(isArmorOrFood);
        toolAttributes.setItemFactory(() -> new ToolAttribute("axe", 0));
        toolAttributes.setCellFactory(view -> new ToolAttributeCell(toolTypeIndex));

        destroySpeed = new DoubleSpinner(0, Double.MAX_VALUE, 0);
        destroySpeed.disableProperty().bind(isArmorOrFood);

        canDestroyAnyBlock = new RadioButton();
        canDestroyAnyBlock.disableProperty().bind(isArmorOrFood);

        attackDamage = new DoubleSpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 1D);

        attackSpeed = new DoubleSpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 4D);
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

        enchantability = new IntegerSpinner(0, Integer.MAX_VALUE, 0);
        enchantability.disableProperty().bind(isFood);

        var enchantmentTypeIndex = indexManager.getIndex(IndexKeys.ENCHANTMENT_TYPE);
        acceptableEnchantments = new CheckComboBox<>(enchantmentTypeIndex.keyList());
        acceptableEnchantments.setConverter(GameDataConverter.create(enchantmentTypeIndex));
        acceptableEnchantments.disableProperty().bind(isFood);

        var attributeIndex = indexManager.getIndex(IndexKeys.ATTRIBUTE);
        attributeModifiers = new TagView<>();
        attributeModifiers.disableProperty().bind(equipmentSlot.valueProperty().isEqualTo(EquipmentSlot.NONE));
        attributeModifiers.setItemFactory(() -> new AttributeModifier("generic.maxHealth", 0, AttributeModifier.Operation.ADD));
        attributeModifiers.setCellFactory(view -> new AttributeModifierCell(attributeIndex));

        repairItem = new ItemPicker(getProject(), 32);
        repairItem.getStyleClass().add("minecraft-small-slot-32x");
        repairItem.disableProperty().bind(isFood);

        recipeRemain = new ItemPicker(getProject(), 32);
        recipeRemain.getStyleClass().add("minecraft-small-slot-32x");

        var useAnimationIndex = indexManager.getIndex(IndexKeys.USE_ANIMATION);
        useAnimation = new ComboBox<>();
        useAnimation.setConverter(GameDataConverter.create(useAnimationIndex));
        useAnimation.setItems(useAnimationIndex.keyList());

        useDuration = new IntegerSpinner(0, Integer.MAX_VALUE, 0);

        hitEntityLoss = new IntegerSpinner(0, Integer.MAX_VALUE, 0);
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

        destroyBlockLoss = new IntegerSpinner(0, Integer.MAX_VALUE, 0);
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

        information = new TextArea();

        var properties = titled(localize("item.properties.title"), form(
                half(localize("item.properties.identifier"), identifier),
                half(localize("item.properties.displayName"), displayName),
                half(localize("item.properties.type"), type),
                half(localize("item.properties.itemGroup"), itemGroup),
                half(localize("item.properties.maxStackSize"), maxStackSize),
                half(localize("item.properties.durability"), durability),
                half(localize("item.properties.equipmentSlot"), equipmentSlot),
                half(localize("item.properties.toolAttributes"), toolAttributes),
                half(localize("item.properties.destroySpeed"), destroySpeed),
                half(localize("item.properties.canDestroyAnyBlock"), canDestroyAnyBlock),
                half(localize("item.properties.attackDamage"), attackDamage),
                half(localize("item.properties.attackSpeed"), attackSpeed),
                half(localize("item.properties.enchantability"), enchantability),
                half(localize("item.properties.acceptableEnchantments"), acceptableEnchantments),
                one(localize("item.properties.attributeModifiers"), attributeModifiers),
                half(localize("item.properties.repairItem"), repairItem),
                half(localize("item.properties.recipeRemain"), recipeRemain),
                half(localize("item.properties.useAnimation"), useAnimation),
                half(localize("item.properties.useDuration"), useDuration),
                half(localize("item.properties.hitEntityLoss"), hitEntityLoss),
                half(localize("item.properties.destroyBlockLoss"), destroyBlockLoss),
                one(localize("item.properties.information"), information)
        ));

        model = new ModelPicker(
                getProject(),
                localize("item.appearance.model"),
                new ResourceStore(
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.MODELS),
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_MODELS), ".json"),
                localize("item.appearance.texture"),
                new ResourceStore(
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_TEXTURES), ".png")
        );
        model.setBlockstate("item");

        hasEffect = new RadioButton();

        armorTexture = new TexturePicker(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ARMOR_TEXTURES)));
        armorTexture.getExtensionFilters().add(ExtensionFilters.PNG);
        armorTexture.setFitSize(128, 64);
//        armorTexture.setMaxSize(128, 64);
        armorTexture.disableProperty().bind(isNotArmor);

        var appearance = titled(localize("item.appearance.title"), form(
                one(model),
                half(localize("item.appearance.hasEffect"), hasEffect),
                one(localize("item.appearance.armorTexture"), armorTexture)
        ));

        fuelBurnTime = new IntegerSpinner(0, Integer.MAX_VALUE, 0);

        var soundEventIndex = indexManager.getIndex(IndexKeys.SOUND_EVENT);
        equipSound = new ComboBox<>();
        equipSound.setConverter(GameDataConverter.create(soundEventIndex));
        equipSound.setItems(soundEventIndex.keyList());
        equipSound.disableProperty().bind(isNotArmor);

        hunger = new IntegerSpinner(0, Integer.MAX_VALUE, 0);
        hunger.disableProperty().bind(isNotFood);

        saturation = new DoubleSpinner(0.0, Double.MAX_VALUE, 0.6);
        saturation.disableProperty().bind(isNotFood);

        isWolfFood = new RadioButton();
        isWolfFood.disableProperty().bind(isNotFood);

        alwaysEdible = new RadioButton();
        alwaysEdible.disableProperty().bind(isNotFood);

        foodContainer = new ItemPicker(getProject(), 32);
        foodContainer.getStyleClass().add("minecraft-small-slot-32x");
        foodContainer.disableProperty().bind(isNotFood);

        var extra = titled(localize("item.extra.title"), form(
                half(localize("item.fuel.fuelBurnTime"), fuelBurnTime),
                half(localize("item.armor.equipSound"), equipSound),
                half(localize("item.food.hunger"), hunger),
                half(localize("item.food.saturation"), saturation),
                half(localize("item.food.isWolfFood"), isWolfFood),
                half(localize("item.food.alwaysEdible"), alwaysEdible),
                half(localize("item.food.foodContainer"), foodContainer)
        ));

        return scrollVBox(properties, appearance, extra);
    }

    @Override
    protected void initialize(ItemElement item) {
        identifier.setText(item.getIdentifier());
        displayName.setText(item.getDisplayName());
        type.setValue(item.getType());
        itemGroup.setValue(item.getItemGroup());
        maxStackSize.setValue(item.getMaxStackSize());
        durability.setValue(item.getDurability());
        equipmentSlot.setValue(item.getEquipmentSlot());
        toolAttributes.getItems().addAll(item.getToolAttributes());
        destroySpeed.setValue(item.getDestroySpeed());
        canDestroyAnyBlock.setSelected(item.isCanDestroyAnyBlock());
        attackDamage.setValue(item.getAttackDamage());
        attackSpeed.setValue(item.getAttackSpeed());
        attributeModifiers.getItems().addAll(item.getAttributeModifiers());
        enchantability.setValue(item.getEnchantability());
        acceptableEnchantments.checkItems(item.getAcceptableEnchantments());
        repairItem.setItem(item.getRepairItem());
        recipeRemain.setItem(item.getRecipeRemain());
        useAnimation.setValue(item.getUseAnimation());
        useDuration.setValue(item.getUseDuration());
        hitEntityLoss.setValue(item.getHitEntityLoss());
        destroyBlockLoss.setValue(item.getDestroyBlockLoss());
        information.setText(StringUtils.join(item.getInformation(), '\n'));

        model.setModel(item.getModel());
        model.setCustomModels(item.getCustomModels());
        model.setTextures(item.getTextures());
        hasEffect.setSelected(item.isHasEffect());
        armorTexture.setResource(item.getArmorTexture());

        fuelBurnTime.setValue(item.getFuelBurnTime());
        equipSound.setValue(item.getEquipSound());
        hunger.setValue(item.getHunger());
        saturation.setValue(item.getSaturation());
        isWolfFood.setSelected(item.isWolfFood());
        alwaysEdible.setSelected(item.isAlwaysEdible());
        foodContainer.setItem(item.getFoodContainer());
    }

    @Override
    protected void updateDataModel(ItemElement item) {
        item.setIdentifier(identifier.getText());
        item.setDisplayName(displayName.getText());
        item.setType(type.getValue());
        item.setItemGroup(itemGroup.getValue());
        item.setMaxStackSize(maxStackSize.getValue());
        item.setDurability(durability.getValue());
        item.setEquipmentSlot(equipmentSlot.getValue());
        item.setToolAttributes(toolAttributes.getItems().toArray(ToolAttribute.EMPTY_ARRAY));
        item.setDestroySpeed(destroySpeed.getValue());
        item.setCanDestroyAnyBlock(canDestroyAnyBlock.isSelected());
        item.setAttackDamage(attackDamage.getValue());
        item.setAttackSpeed(attackSpeed.getValue());
        item.setAttributeModifiers(attributeModifiers.getItems().toArray(AttributeModifier.EMPTY_ARRAY));
        item.setEnchantability(enchantability.getValue());
        item.setAcceptableEnchantments(acceptableEnchantments.getCheckedItems().toArray(ArrayUtils.EMPTY_STRING_ARRAY));
        item.setRepairItem(repairItem.getItem());
        item.setRecipeRemain(recipeRemain.getItem());
        item.setUseAnimation(useAnimation.getValue());
        item.setUseDuration(useDuration.getValue());
        item.setHitEntityLoss(hitEntityLoss.getValue());
        item.setDestroyBlockLoss(destroyBlockLoss.getValue());
        item.setInformation(StringUtils.splitByLineSeparator(information.getText()));

        item.setModel(model.getModel());
        item.setCustomModels(model.getCustomModels());
        item.setTextures(model.getTextures());
        item.setHasEffect(hasEffect.isSelected());
        item.setArmorTexture(armorTexture.getResource());

        item.setFuelBurnTime(fuelBurnTime.getValue());
        item.setEquipSound(equipSound.getValue());
        item.setHunger(hunger.getValue());
        item.setSaturation(saturation.getValue());
        item.setWolfFood(isWolfFood.isSelected());
        item.setAlwaysEdible(alwaysEdible.isSelected());
        item.setFoodContainer(foodContainer.getItem());
    }

    @Override
    protected boolean validate() {
        return Validator.validate(identifier);
    }
}
