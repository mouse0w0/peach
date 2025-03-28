package com.github.mouse0w0.peach.mcmod.element.impl;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.LocalizableElement;
import com.github.mouse0w0.peach.util.ArrayUtils;

import java.util.Collections;
import java.util.Map;

public class ItemElement extends Element implements LocalizableElement {

    private String identifier;
    private String displayName;
    private ItemType type = ItemType.NORMAL;
    private String itemGroup;
    private int maxStackSize = 64;
    private int durability;
    private double destroySpeed = 1D;
    private boolean canDestroyAnyBlock;
    private ToolAttribute[] toolAttributes = ToolAttribute.EMPTY_ARRAY;
    private AttributeModifier[] attributeModifiers = AttributeModifier.EMPTY_ARRAY;
    private int enchantability;
    private String[] acceptableEnchantments = ArrayUtils.EMPTY_STRING_ARRAY;
    private IdMetadata repairItem = IdMetadata.air();
    private IdMetadata recipeRemain = IdMetadata.air();
    private EquipmentSlot equipmentSlot = EquipmentSlot.NONE;
    private String useAnimation = "NONE";
    private int useDuration;
    private double attackDamage = 1D;
    private double attackSpeed = 4D;
    private int hitEntityLoss;
    private int destroyBlockLoss;
    private String[] information = ArrayUtils.EMPTY_STRING_ARRAY;

    private Identifier model;
    private Map<String, String> customModels = Collections.emptyMap();
    private Map<String, String> textures = Collections.emptyMap();
    private boolean hasEffect = false;
    private String armorTexture;

    private int fuelBurnTime;
    private String equipSound;
    private int hunger;
    private double saturation = 0.6D;
    private boolean isWolfFood;
    private boolean alwaysEdible;
    private IdMetadata foodContainer = IdMetadata.air();

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

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
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

    public String[] getAcceptableEnchantments() {
        return acceptableEnchantments;
    }

    public void setAcceptableEnchantments(String[] acceptableEnchantments) {
        this.acceptableEnchantments = acceptableEnchantments;
    }

    public IdMetadata getRepairItem() {
        return repairItem;
    }

    public void setRepairItem(IdMetadata repairItem) {
        this.repairItem = repairItem;
    }

    public IdMetadata getRecipeRemain() {
        return recipeRemain;
    }

    public void setRecipeRemain(IdMetadata recipeRemain) {
        this.recipeRemain = recipeRemain;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    public void setEquipmentSlot(EquipmentSlot equipmentSlot) {
        this.equipmentSlot = equipmentSlot;
    }

    public String getUseAnimation() {
        return useAnimation;
    }

    public void setUseAnimation(String useAnimation) {
        this.useAnimation = useAnimation;
    }

    public int getUseDuration() {
        return useDuration;
    }

    public void setUseDuration(int useDuration) {
        this.useDuration = useDuration;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
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

    public String[] getInformation() {
        return information;
    }

    public void setInformation(String[] information) {
        this.information = information;
    }

    public Identifier getModel() {
        return model;
    }

    public void setModel(Identifier model) {
        this.model = model;
    }

    public Map<String, String> getCustomModels() {
        return customModels;
    }

    public void setCustomModels(Map<String, String> customModels) {
        this.customModels = customModels;
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

    public String getEquipSound() {
        return equipSound;
    }

    public void setEquipSound(String equipSound) {
        this.equipSound = equipSound;
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

    public IdMetadata getFoodContainer() {
        return foodContainer;
    }

    public void setFoodContainer(IdMetadata foodContainer) {
        this.foodContainer = foodContainer;
    }

    @Override
    public void getTranslation(String namespace, Map<String, String> translation) {
        translation.put("item." + namespace + "." + identifier + ".name", displayName);

        final String prefix = "item." + namespace + "." + identifier + ".tooltip.";
        for (int i = 0; i < information.length; i++) {
            translation.put(prefix + i, information[i]);
        }
    }
}
