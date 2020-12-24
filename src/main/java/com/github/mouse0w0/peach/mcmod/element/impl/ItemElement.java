package com.github.mouse0w0.peach.mcmod.element.impl;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.language.Localizable;

import java.util.Collections;
import java.util.Map;

public class ItemElement extends Element implements Localizable {

    private String identifier;
    private String displayName;
    private ItemType itemType = ItemType.NORMAL;
    private String itemGroup;
    private int maxStackSize = 64;
    private int durability = 0;
    private double destroySpeed = 0;
    private boolean canDestroyAnyBlock = false;
    private ToolAttribute[] toolAttributes = ToolAttribute.EMPTY_ARRAY;
    private AttributeModifier[] attributeModifiers = AttributeModifier.EMPTY_ARRAY;
    private int enchantability = 0;
    private EnchantmentType[] acceptableEnchantments = EnchantmentType.EMPTY_ARRAY;
    private Item repairItem;
    private Item recipeRemain;
    private EquipmentSlot equipmentSlot = EquipmentSlot.MAINHAND;
    private UseAnimation useAnimation = UseAnimation.NONE;
    private int useDuration = 0;
    private int fuelBurnTime = 0;
    private boolean hasEffect = false;
    private String information;

    private String model = "generated";
    private Map<String, String> textures = Collections.emptyMap();

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

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
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

    public EnchantmentType[] getAcceptableEnchantments() {
        return acceptableEnchantments;
    }

    public void setAcceptableEnchantments(EnchantmentType[] acceptableEnchantments) {
        this.acceptableEnchantments = acceptableEnchantments;
    }

    public Item getRepairItem() {
        return repairItem;
    }

    public void setRepairItem(Item repairItem) {
        this.repairItem = repairItem;
    }

    public Item getRecipeRemain() {
        return recipeRemain;
    }

    public void setRecipeRemain(Item recipeRemain) {
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Map<String, String> getTextures() {
        return textures;
    }

    public void setTextures(Map<String, String> textures) {
        this.textures = textures;
    }

    @Override
    public void getLocalizedText(String namespace, Map<String, String> localizedTexts) {
        localizedTexts.put("item." + namespace + "." + identifier + ".name", displayName);
    }
}
