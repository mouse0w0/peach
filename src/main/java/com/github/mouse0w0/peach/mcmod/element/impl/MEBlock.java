package com.github.mouse0w0.peach.mcmod.element.impl;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.LocalizableElement;
import org.joml.primitives.AABBd;

import java.util.Collections;
import java.util.Map;

public class MEBlock extends Element implements LocalizableElement {
    private String identifier;
    private String displayName;
    private BlockType type = BlockType.NORMAL;
    private Material material;
    private ItemGroup itemGroup;
    private SoundType soundType;
    private double hardness;
    private boolean unbreakable; // set hardness -1.
    private double resistance;
    private double slipperiness;
    private int brightness;
    private int opacity = 255;
    //    private boolean gravity; // as block type?
    private String harvestTool = ToolType.NONE;
    private int harvestLevel;
    private String information;

    private Identifier modelPrototype;
    private Map<String, String> models = Collections.emptyMap();
    private Map<String, String> textures = Collections.emptyMap();
    private String particleTexture;
    private boolean transparency;
    private RenderType renderType = RenderType.SOLID;
    private OffsetType offsetType = OffsetType.NONE;
    private Identifier itemModelPrototype;
    private Map<String, String> itemModels = Collections.emptyMap();
    private Map<String, String> itemTextures = Collections.emptyMap();

    private AABBd boundingBox = new AABBd(0, 0, 0, 16, 16, 16);
    private boolean noCollision;

    //    private boolean enableLootTable; // 1.14+
    private ItemStack dropItem;

    private boolean doNotRegisterItem;
    private MapColor mapColor = MapColor.INHERIT;
    private String beaconColor = "0x00000000";
    private boolean beaconBase;
    private boolean climbable;
    private boolean replaceable; // what?
    private boolean canConnectRedstone;
    private int redstonePower;
    private PlantType canPlantPlant = PlantType.NONE;
    private double enchantPowerBonus;
    private int flammability;
    private int fireSpreadSpeed;
    private PushReaction pushReaction = PushReaction.INHERIT;
    private PathNodeType aiPathNodeType = PathNodeType.INHERIT;
//    private ItemRef pickItem;

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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public SoundType getSoundType() {
        return soundType;
    }

    public void setSoundType(SoundType soundType) {
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

    public RenderType getRenderType() {
        return renderType;
    }

    public void setRenderType(RenderType renderType) {
        this.renderType = renderType;
    }

    public OffsetType getOffsetType() {
        return offsetType;
    }

    public void setOffsetType(OffsetType offsetType) {
        this.offsetType = offsetType;
    }

    public Identifier getItemModelPrototype() {
        return itemModelPrototype;
    }

    public void setItemModelPrototype(Identifier itemModelPrototype) {
        this.itemModelPrototype = itemModelPrototype;
    }

    public Map<String, String> getItemModels() {
        return itemModels;
    }

    public void setItemModels(Map<String, String> itemModels) {
        this.itemModels = itemModels;
    }

    public Map<String, String> getItemTextures() {
        return itemTextures;
    }

    public void setItemTextures(Map<String, String> itemTextures) {
        this.itemTextures = itemTextures;
    }

    public AABBd getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(AABBd boundingBox) {
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

    public MapColor getMapColor() {
        return mapColor;
    }

    public void setMapColor(MapColor mapColor) {
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

    public PlantType getCanPlantPlant() {
        return canPlantPlant;
    }

    public void setCanPlantPlant(PlantType canPlantPlant) {
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

    public PushReaction getPushReaction() {
        return pushReaction;
    }

    public void setPushReaction(PushReaction pushReaction) {
        this.pushReaction = pushReaction;
    }

    public PathNodeType getAiPathNodeType() {
        return aiPathNodeType;
    }

    public void setAiPathNodeType(PathNodeType aiPathNodeType) {
        this.aiPathNodeType = aiPathNodeType;
    }

//    public ItemRef getPickItem() {
//        return pickItem;
//    }
//
//    public void setPickItem(ItemRef pickItem) {
//        this.pickItem = pickItem;
//    }

    @Override
    public void getTranslation(String namespace, Map<String, String> translation) {

    }
}
