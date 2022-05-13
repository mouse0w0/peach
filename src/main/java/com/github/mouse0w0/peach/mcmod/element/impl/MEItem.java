package com.github.mouse0w0.peach.mcmod.element.impl;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.LocalizableElement;
import com.google.common.base.Strings;

import java.util.Collections;
import java.util.Map;

public class MEItem extends Element implements LocalizableElement {

    private String identifier;
    private String displayName;
    private ItemType type = ItemType.NORMAL;
    private ItemGroup itemGroup;
    private int maxStackSize = 64;
    private int durability;
    private double destroySpeed = 1;
    private boolean canDestroyAnyBlock;
    private ToolAttribute[] toolAttributes = ToolAttribute.EMPTY_ARRAY;
    private AttributeModifier[] attributeModifiers = AttributeModifier.EMPTY_ARRAY;
    private int enchantability;
    private EnchantmentType[] acceptableEnchantments = EnchantmentType.EMPTY_ARRAY;
    private ItemRef repairItem = ItemRef.AIR;
    private ItemRef recipeRemain = ItemRef.AIR;
    private EquipmentSlot equipmentSlot = EquipmentSlot.MAINHAND;
    private UseAnimation useAnimation = UseAnimation.NONE;
    private int useDuration;
    private int hitEntityLoss;
    private int destroyBlockLoss;
    private String information;

    private Identifier modelPrototype;
    private Map<String, String> models = Collections.emptyMap();
    private Map<String, String> textures = Collections.emptyMap();
    private boolean hasEffect = false;
    private String armorTexture;

    private int fuelBurnTime;
    private int hunger;
    private double saturation = 0.6;
    private boolean isWolfFood;
    private boolean alwaysEdible;
    private ItemRef foodContainer = ItemRef.AIR;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public double getDestroySpeed() {
        return destroySpeed;
    }

    public void setDestroySpeed(double destroySpeed) {
        this.destroySpeed = destroySpeed;
    }

    public boolean isCanDestroyAnyBlock() {
        return canDestroyAnyBlock;
    }

    public void setCanDestroyAnyBlock(boolean canDestroyAnyBlock) {
        this.canDestroyAnyBlock = canDestroyAnyBlock;
    }

    public ToolAttribute[] getToolAttributes() {
        return toolAttributes;
    }

    public void setToolAttributes(ToolAttribute[] toolAttributes) {
        this.toolAttributes = toolAttributes;
    }

    public AttributeModifier[] getAttributeModifiers() {
        return attributeModifiers;
    }

    public void setAttributeModifiers(AttributeModifier[] attributeModifiers) {
        this.attributeModifiers = attributeModifiers;
    }

    public int getEnchantability() {
        return enchantability;
    }

    public void setEnchantability(int enchantability) {
        this.enchantability = enchantability;
    }

    public EnchantmentType[] getAcceptableEnchantments() {
        return acceptableEnchantments;
    }

    public void setAcceptableEnchantments(EnchantmentType[] acceptableEnchantments) {
        this.acceptableEnchantments = acceptableEnchantments;
    }

    public ItemRef getRepairItem() {
        return repairItem;
    }

    public void setRepairItem(ItemRef repairItem) {
        this.repairItem = repairItem;
    }

    public ItemRef getRecipeRemain() {
        return recipeRemain;
    }

    public void setRecipeRemain(ItemRef recipeRemain) {
        this.recipeRemain = recipeRemain;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    public void setEquipmentSlot(EquipmentSlot equipmentSlot) {
        this.equipmentSlot = equipmentSlot;
    }

    public UseAnimation getUseAnimation() {
        return useAnimation;
    }

    public void setUseAnimation(UseAnimation useAnimation) {
        this.useAnimation = useAnimation;
    }

    public int getUseDuration() {
        return useDuration;
    }

    public void setUseDuration(int useDuration) {
        this.useDuration = useDuration;
    }

    public int getHitEntityLoss() {
        return hitEntityLoss;
    }

    public void setHitEntityLoss(int hitEntityLoss) {
        this.hitEntityLoss = hitEntityLoss;
    }

    public int getDestroyBlockLoss() {
        return destroyBlockLoss;
    }

    public void setDestroyBlockLoss(int destroyBlockLoss) {
        this.destroyBlockLoss = destroyBlockLoss;
    }

    public int getFuelBurnTime() {
        return fuelBurnTime;
    }

    public void setFuelBurnTime(int fuelBurnTime) {
        this.fuelBurnTime = fuelBurnTime;
    }

    public boolean isHasEffect() {
        return hasEffect;
    }

    public void setHasEffect(boolean hasEffect) {
        this.hasEffect = hasEffect;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Identifier getModelPrototype() {
        return modelPrototype;
    }

    public void setModelPrototype(Identifier modelPrototype) {
        this.modelPrototype = modelPrototype;
    }

    public Map<String, String> getModels() {
        return models;
    }

    public void setModels(Map<String, String> models) {
        this.models = models;
    }

    public Map<String, String> getTextures() {
        return textures;
    }

    public void setTextures(Map<String, String> textures) {
        this.textures = textures;
    }

    public String getArmorTexture() {
        return armorTexture;
    }

    public void setArmorTexture(String armorTexture) {
        this.armorTexture = armorTexture;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public double getSaturation() {
        return saturation;
    }

    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    public boolean isWolfFood() {
        return isWolfFood;
    }

    public void setWolfFood(boolean wolfFood) {
        isWolfFood = wolfFood;
    }

    public boolean isAlwaysEdible() {
        return alwaysEdible;
    }

    public void setAlwaysEdible(boolean alwaysEdible) {
        this.alwaysEdible = alwaysEdible;
    }

    public ItemRef getFoodContainer() {
        return foodContainer;
    }

    public void setFoodContainer(ItemRef foodContainer) {
        this.foodContainer = foodContainer;
    }

    @Override
    public void getTranslation(String namespace, Map<String, String> translation) {
        translation.put("item." + namespace + "." + identifier + ".name", displayName);

        if (!Strings.isNullOrEmpty(information)) {
            String prefix = "item." + namespace + "." + identifier + ".tooltip.";
            String[] split = information.split("\n");
            for (int i = 0; i < split.length; i++) {
                translation.put(prefix + i, split[i]);
            }
        }
    }
}
