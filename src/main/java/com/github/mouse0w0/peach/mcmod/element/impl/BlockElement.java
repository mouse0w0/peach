package com.github.mouse0w0.peach.mcmod.element.impl;

import com.github.mouse0w0.peach.mcmod.BlockType;
import com.github.mouse0w0.peach.mcmod.BoundingBox;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.LocalizableElement;
import com.github.mouse0w0.peach.util.ArrayUtils;

import java.util.Collections;
import java.util.Map;

public class BlockElement extends Element implements LocalizableElement {
    public static final BoundingBox FULL_BLOCK = new BoundingBox(0, 0, 0, 1, 1, 1);

    private String identifier;
    private String displayName;
    private BlockType type = BlockType.NORMAL;
    private String itemGroup;
    private String material = "ROCK";
    private String soundType = "STONE";
    private double hardness;
    private boolean unbreakable; // set hardness -1.
    private double resistance;
    private double slipperiness = 0.6;
    private int brightness;
    private int opacity = 255;
    //    private boolean gravity; // as block type?
    private String harvestTool = "NONE";
    private int harvestLevel;
    private String[] information = ArrayUtils.EMPTY_STRING_ARRAY;

    private Identifier model;
    private Map<String, String> customModels = Collections.emptyMap();
    private Map<String, String> textures = Collections.emptyMap();
    private String particleTexture;
    private boolean transparency;
    private String renderType = "SOLID";
    private String offsetType = "NONE";
    private Identifier itemModel;
    private Map<String, String> customItemModels = Collections.emptyMap();
    private Map<String, String> itemTextures = Collections.emptyMap();

    private BoundingBox boundingBox = FULL_BLOCK;
    private boolean noCollision;

    //    private boolean enableLootTable; // 1.14+
    private ItemStack dropItem;

    private boolean doNotRegisterItem;
    private String mapColor = "INHERIT";
    private String beaconColor = "0x00000000";
    private boolean beaconBase;
    private boolean climbable;
    private boolean replaceable; // what?
    private boolean canConnectRedstone;
    private int redstonePower;
    private String canPlantPlant = "NONE";
    private double enchantPowerBonus;
    private int flammability;
    private int fireSpreadSpeed;
    private String pushReaction = "INHERIT";
    private String aiPathNodeType = "INHERIT";
//    private IdMetadata pickItem;

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

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getSoundType() {
        return soundType;
    }

    public void setSoundType(String soundType) {
        this.soundType = soundType;
    }

    public double getHardness() {
        return hardness;
    }

    public void setHardness(double hardness) {
        this.hardness = hardness;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public double getSlipperiness() {
        return slipperiness;
    }

    public void setSlipperiness(double slipperiness) {
        this.slipperiness = slipperiness;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public String getHarvestTool() {
        return harvestTool;
    }

    public void setHarvestTool(String harvestTool) {
        this.harvestTool = harvestTool;
    }

    public int getHarvestLevel() {
        return harvestLevel;
    }

    public void setHarvestLevel(int harvestLevel) {
        this.harvestLevel = harvestLevel;
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

    public String getParticleTexture() {
        return particleTexture;
    }

    public void setParticleTexture(String particleTexture) {
        this.particleTexture = particleTexture;
    }

    public boolean isTransparency() {
        return transparency;
    }

    public void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public String getOffsetType() {
        return offsetType;
    }

    public void setOffsetType(String offsetType) {
        this.offsetType = offsetType;
    }

    public Identifier getItemModel() {
        return itemModel;
    }

    public void setItemModel(Identifier itemModel) {
        this.itemModel = itemModel;
    }

    public Map<String, String> getCustomItemModels() {
        return customItemModels;
    }

    public void setCustomItemModels(Map<String, String> customItemModels) {
        this.customItemModels = customItemModels;
    }

    public Map<String, String> getItemTextures() {
        return itemTextures;
    }

    public void setItemTextures(Map<String, String> itemTextures) {
        this.itemTextures = itemTextures;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public boolean isNoCollision() {
        return noCollision;
    }

    public void setNoCollision(boolean noCollision) {
        this.noCollision = noCollision;
    }

//    public ItemStack getDropItem() {
//        return dropItem;
//    }
//
//    public void setDropItem(ItemStack dropItem) {
//        this.dropItem = dropItem;
//    }

    public boolean isDoNotRegisterItem() {
        return doNotRegisterItem;
    }

    public void setDoNotRegisterItem(boolean doNotRegisterItem) {
        this.doNotRegisterItem = doNotRegisterItem;
    }

    public String getMapColor() {
        return mapColor;
    }

    public void setMapColor(String mapColor) {
        this.mapColor = mapColor;
    }

    public String getBeaconColor() {
        return beaconColor;
    }

    public void setBeaconColor(String beaconColor) {
        this.beaconColor = beaconColor;
    }

    public boolean isBeaconBase() {
        return beaconBase;
    }

    public void setBeaconBase(boolean beaconBase) {
        this.beaconBase = beaconBase;
    }

    public boolean isClimbable() {
        return climbable;
    }

    public void setClimbable(boolean climbable) {
        this.climbable = climbable;
    }

    public boolean isReplaceable() {
        return replaceable;
    }

    public void setReplaceable(boolean replaceable) {
        this.replaceable = replaceable;
    }

    public boolean isCanConnectRedstone() {
        return canConnectRedstone;
    }

    public void setCanConnectRedstone(boolean canConnectRedstone) {
        this.canConnectRedstone = canConnectRedstone;
    }

    public int getRedstonePower() {
        return redstonePower;
    }

    public void setRedstonePower(int redstonePower) {
        this.redstonePower = redstonePower;
    }

    public String getCanPlantPlant() {
        return canPlantPlant;
    }

    public void setCanPlantPlant(String canPlantPlant) {
        this.canPlantPlant = canPlantPlant;
    }

    public double getEnchantPowerBonus() {
        return enchantPowerBonus;
    }

    public void setEnchantPowerBonus(double enchantPowerBonus) {
        this.enchantPowerBonus = enchantPowerBonus;
    }

    public int getFlammability() {
        return flammability;
    }

    public void setFlammability(int flammability) {
        this.flammability = flammability;
    }

    public int getFireSpreadSpeed() {
        return fireSpreadSpeed;
    }

    public void setFireSpreadSpeed(int fireSpreadSpeed) {
        this.fireSpreadSpeed = fireSpreadSpeed;
    }

    public String getPushReaction() {
        return pushReaction;
    }

    public void setPushReaction(String pushReaction) {
        this.pushReaction = pushReaction;
    }

    public String getAiPathNodeType() {
        return aiPathNodeType;
    }

    public void setAiPathNodeType(String aiPathNodeType) {
        this.aiPathNodeType = aiPathNodeType;
    }

//    public IdMetadata getPickItem() {
//        return pickItem;
//    }
//
//    public void setPickItem(IdMetadata pickItem) {
//        this.pickItem = pickItem;
//    }

    @Override
    public void getTranslation(String namespace, Map<String, String> translation) {
        translation.put("tile." + namespace + "." + identifier + ".name", displayName);

        final String prefix = "tile." + namespace + "." + identifier + ".tooltip.";
        for (int i = 0; i < information.length; i++) {
            translation.put(prefix + i, information[i]);
        }
    }
}
