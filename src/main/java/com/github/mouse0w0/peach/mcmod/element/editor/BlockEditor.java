package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.impl.MEBlock;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.mcmod.model.Blockstate;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.ui.LocalizableConverter;
import com.github.mouse0w0.peach.mcmod.ui.cell.LocalizableWithItemIconCell;
import com.github.mouse0w0.peach.mcmod.ui.form.ModelField;
import com.github.mouse0w0.peach.mcmod.ui.form.ModelTextureField;
import com.github.mouse0w0.peach.mcmod.ui.form.TextureField;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
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
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;

public class BlockEditor extends ElementEditor<MEBlock> {
    private final IndexManager indexManager;

    private Form form;

    private TextFieldField identifier;
    private TextFieldField displayName;
    private ChoiceBoxField<BlockType> type;
    private ComboBoxField<Material> material;
    private ComboBoxField<ItemGroup> itemGroup;
    private ComboBoxField<SoundType> soundType;
    private DoubleField hardness;
    private RadioButtonField unbreakable;
    private DoubleField resistance;
    private DoubleField slipperiness;
    private IntegerField brightness;
    private IntegerField opacity;
    private ChoiceBoxField<String> harvestTool;
    private IntegerField harvestLevel;
    private RadioButtonField replaceable;
    private TextAreaField information;

    private DoubleField minX;
    private DoubleField minY;
    private DoubleField minZ;
    private DoubleField maxX;
    private DoubleField maxY;
    private DoubleField maxZ;
    private RadioButtonField noCollision;

    private ModelField model;
    private ModelTextureField textures;
    private TextureField particleTexture;
    private RadioButtonField transparency;
    private ChoiceBoxField<RenderType> renderType;
    private ChoiceBoxField<OffsetType> offsetType;
    private ModelField itemModel;
    private ModelTextureField itemTextures;

//    private Object dropItem; // TODO

    private RadioButtonField doNotRegisterItem;
    private ComboBoxField<MapColor> mapColor;
    private ColorPickerField beaconColor;
    private RadioButtonField beaconBase;
    private RadioButtonField climbable;
    private RadioButtonField canConnectRedstone;
    private IntegerField redstonePower;
    private ChoiceBoxField<PlantType> canPlantPlant;
    private DoubleField enchantPowerBonus;
    private IntegerField flammability;
    private IntegerField fireSpreadSpeed;
    private ChoiceBoxField<PushReaction> pushReaction;
    private ChoiceBoxField<PathNodeType> aiPathNodeType;
//    private Object pickItem; // TODO

    public BlockEditor(@NotNull Project project, @NotNull MEBlock element) {
        super(project, element);
        indexManager = IndexManager.getInstance(project);
    }

    @Override
    protected Node getContent() {
        form = new Form();

        identifier = new TextFieldField();
        identifier.getChecks().add(Check.of(AppL10n.localize("validate.invalidIdentifier"), ModUtils::validateIdentifier));
        identifier.setLabel(AppL10n.localize("block.properties.identifier"));
        identifier.setColSpan(ColSpan.HALF);

        displayName = new TextFieldField();
        displayName.setLabel(AppL10n.localize("block.properties.displayName"));
        displayName.setColSpan(ColSpan.HALF);

        type = new ChoiceBoxField<>();
        type.setLabel(AppL10n.localize("block.properties.type"));
        type.setConverter(LocalizableConverter.instance());
        type.getItems().addAll(BlockType.values());
        type.setValue(BlockType.NORMAL);
        type.setColSpan(ColSpan.HALF);

        material = new ComboBoxField<>();
        material.setLabel(AppL10n.localize("block.properties.material"));
        material.setCellFactory(LocalizableWithItemIconCell.factory());
        material.setButtonCell(LocalizableWithItemIconCell.create());
        material.getItems().addAll(indexManager.getIndex(Indexes.MATERIALS).values());
        material.setColSpan(ColSpan.HALF);

        itemGroup = new ComboBoxField<>();
        itemGroup.setLabel(AppL10n.localize("block.properties.itemGroup"));
        itemGroup.setCellFactory(LocalizableWithItemIconCell.factory());
        itemGroup.setButtonCell(LocalizableWithItemIconCell.create());
        itemGroup.getItems().addAll(indexManager.getIndex(Indexes.ITEM_GROUPS).values());
        itemGroup.setColSpan(ColSpan.HALF);

        soundType = new ComboBoxField<>();
        soundType.setLabel(AppL10n.localize("block.properties.soundType"));
        soundType.setCellFactory(LocalizableWithItemIconCell.factory());
        soundType.setButtonCell(LocalizableWithItemIconCell.create());
        soundType.getItems().addAll(indexManager.getIndex(Indexes.SOUND_TYPES).values());
        soundType.setColSpan(ColSpan.HALF);

        hardness = new DoubleField(0D, Double.MAX_VALUE, 0D);
        hardness.setLabel(AppL10n.localize("block.properties.hardness"));
        hardness.setColSpan(ColSpan.HALF);

        unbreakable = new RadioButtonField();
        unbreakable.setLabel(AppL10n.localize("block.properties.unbreakable"));
        unbreakable.setColSpan(ColSpan.HALF);
        hardness.disableProperty().bind(unbreakable.valueProperty());

        resistance = new DoubleField(0D, Double.MAX_VALUE, 0D);
        resistance.setLabel(AppL10n.localize("block.properties.resistance"));
        resistance.setColSpan(ColSpan.HALF);

        slipperiness = new DoubleField(0D, 1D, 0.6D);
        slipperiness.setLabel(AppL10n.localize("block.properties.slipperiness"));
        slipperiness.setColSpan(ColSpan.HALF);

        brightness = new IntegerField(0, 15, 0);
        brightness.setLabel(AppL10n.localize("block.properties.brightness"));
        brightness.setColSpan(ColSpan.HALF);

        opacity = new IntegerField(0, 255, 255);
        opacity.setLabel(AppL10n.localize("block.properties.opacity"));
        opacity.setColSpan(ColSpan.HALF);

        harvestTool = new ChoiceBoxField<>();
        harvestTool.setLabel(AppL10n.localize("block.properties.harvestTool"));
        harvestTool.setColSpan(ColSpan.HALF);
        harvestTool.setConverter(new StringConverter<>() {
            @Override
            public String toString(String object) {
                return AppL10n.localize("toolType." + object);
            }

            @Override
            public String fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        harvestTool.getItems().add(ToolType.NONE);
        harvestTool.getItems().addAll(ToolType.getToolTypes());

        harvestLevel = new IntegerField(0, Integer.MAX_VALUE, 0);
        harvestLevel.setLabel(AppL10n.localize("block.properties.harvestLevel"));
        harvestLevel.setColSpan(ColSpan.HALF);
        harvestLevel.disableProperty().bind(harvestTool.valueProperty().isEqualTo(ToolType.NONE));

        information = new TextAreaField();
        information.setLabel(AppL10n.localize("block.properties.information"));

        Section properties = new Section();
        properties.setText(AppL10n.localize("block.properties.title"));
        properties.getElements().addAll(
                identifier, displayName,
                type, material,
                itemGroup, soundType,
                hardness, unbreakable,
                resistance, slipperiness,
                brightness, opacity,
                harvestTool, harvestLevel,
                information);

        model = new ModelField(getProject(), new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.MODELS),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.BLOCK_MODELS), ".json"));
        model.setText(AppL10n.localize("block.appearance.model"));
        model.blockstateProperty().bind(Bindings.createStringBinding(() -> type.getValue().getBlockstate(), type.valueProperty()));

        textures = new ModelTextureField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.BLOCK_TEXTURES), ".png"));
        textures.setLabel(AppL10n.localize("block.appearance.texture"));
        model.getTextures().addListener((InvalidationListener) observable -> textures.setTextureKeys(model.getTextures()));

        particleTexture = new TextureField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.BLOCK_TEXTURES), ".png"));
        particleTexture.setLabel(AppL10n.localize("block.appearance.particleTexture"));
        particleTexture.setFitSize(64, 64);

        transparency = new RadioButtonField();
        transparency.setLabel(AppL10n.localize("block.appearance.transparency"));
        transparency.setColSpan(ColSpan.HALF);
        transparency.valueProperty().addListener(observable -> opacity.setValue(transparency.getValue() ? 0 : 255));

        renderType = new ChoiceBoxField<>();
        renderType.setLabel(AppL10n.localize("block.appearance.renderType"));
        renderType.setConverter(LocalizableConverter.instance());
        renderType.getItems().addAll(RenderType.values());
        renderType.setColSpan(ColSpan.HALF);

        offsetType = new ChoiceBoxField<>();
        offsetType.setLabel(AppL10n.localize("block.appearance.offsetType"));
        offsetType.setConverter(LocalizableConverter.instance());
        offsetType.getItems().addAll(OffsetType.values());
        offsetType.setColSpan(ColSpan.HALF);

        itemModel = new ModelField(getProject(), new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.MODELS),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_MODELS), ".json"));
        itemModel.setText(AppL10n.localize("block.appearance.itemModel"));
        itemModel.setBlockstate("item");
        itemModel.inheritProperty().bind(Bindings.createBooleanBinding(() -> {
            ModelManager modelManager = ModelManager.getInstance();
            Blockstate blockstate = modelManager.getBlockstate(type.getValue().getBlockstate());
            if (blockstate.getItem() == null) return false;
            return modelManager.hasModelProperty(blockstate.getItem());
        }, type.valueProperty()));

        itemTextures = new ModelTextureField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_TEXTURES), ".png"));
        itemTextures.setLabel(AppL10n.localize("block.appearance.itemTexture"));
        itemTextures.visibleProperty().bind(Bindings.isNotEmpty(itemModel.getTextures()));
        itemModel.getTextures().addListener((InvalidationListener) observable -> itemTextures.setTextureKeys(itemModel.getTextures()));

        Section appearance = new Section();
        appearance.setText(AppL10n.localize("block.appearance.title"));
        appearance.getElements().addAll(
                model,
                textures,
                particleTexture,
                transparency, renderType,
                offsetType,
                itemModel,
                itemTextures
        );

        minX = new DoubleField(0D, 16D, 0D);
        minX.setLabel(AppL10n.localize("block.collision.minX"));
        minX.setColSpan(ColSpan.THIRD);

        minY = new DoubleField(0D, 24D, 0D);
        minY.setLabel(AppL10n.localize("block.collision.minY"));
        minY.setColSpan(ColSpan.THIRD);

        minZ = new DoubleField(0D, 16D, 0D);
        minZ.setLabel(AppL10n.localize("block.collision.minZ"));
        minZ.setColSpan(ColSpan.THIRD);

        maxX = new DoubleField(0D, 16D, 16D);
        maxX.setLabel(AppL10n.localize("block.collision.maxX"));
        maxX.setColSpan(ColSpan.THIRD);

        maxY = new DoubleField(0D, 24D, 16D);
        maxY.setLabel(AppL10n.localize("block.collision.maxY"));
        maxY.setColSpan(ColSpan.THIRD);

        maxZ = new DoubleField(0D, 16D, 16D);
        maxZ.setLabel(AppL10n.localize("block.collision.maxZ"));
        maxZ.setColSpan(ColSpan.THIRD);

        noCollision = new RadioButtonField();
        noCollision.setLabel(AppL10n.localize("block.collision.noCollision"));
        noCollision.setColSpan(ColSpan.HALF);

        Section collision = new Section();
        collision.setText(AppL10n.localize("block.collision.title"));
        collision.getElements().addAll(
                minX, minY, minZ,
                maxX, maxY, maxZ,
                noCollision);

        doNotRegisterItem = new RadioButtonField();
        doNotRegisterItem.setLabel(AppL10n.localize("block.extra.doNotRegisterItem"));
        doNotRegisterItem.setColSpan(ColSpan.HALF);

        mapColor = new ComboBoxField<>();
        mapColor.setLabel(AppL10n.localize("block.extra.mapColor"));
        mapColor.setCellFactory(LocalizableWithItemIconCell.factory());
        mapColor.setButtonCell(LocalizableWithItemIconCell.create());
        mapColor.getItems().addAll(indexManager.getIndex(Indexes.MAP_COLORS).values());
        mapColor.setColSpan(ColSpan.HALF);

        beaconColor = new ColorPickerField();
        beaconColor.setLabel(AppL10n.localize("block.extra.beaconColor"));
        beaconColor.setColSpan(ColSpan.HALF);

        beaconBase = new RadioButtonField();
        beaconBase.setLabel(AppL10n.localize("block.extra.beaconBase"));
        beaconBase.setColSpan(ColSpan.HALF);

        climbable = new RadioButtonField();
        climbable.setLabel(AppL10n.localize("block.extra.climbable"));
        climbable.setColSpan(ColSpan.HALF);

        replaceable = new RadioButtonField();
        replaceable.setLabel(AppL10n.localize("block.extra.replaceable"));
        replaceable.setColSpan(ColSpan.HALF);

        canConnectRedstone = new RadioButtonField();
        canConnectRedstone.setLabel(AppL10n.localize("block.extra.canConnectRedstone"));
        canConnectRedstone.setColSpan(ColSpan.HALF);

        redstonePower = new IntegerField(0, 15, 0);
        redstonePower.setLabel(AppL10n.localize("block.extra.redstonePower"));
        redstonePower.setColSpan(ColSpan.HALF);

        canPlantPlant = new ChoiceBoxField<>();
        canPlantPlant.setLabel(AppL10n.localize("block.extra.canPlantPlant"));
        canPlantPlant.setConverter(LocalizableConverter.instance());
        canPlantPlant.getItems().addAll(PlantType.values());
        canPlantPlant.setColSpan(ColSpan.HALF);

        enchantPowerBonus = new DoubleField(0, Double.MAX_VALUE, 0);
        enchantPowerBonus.setLabel(AppL10n.localize("block.extra.enchantPowerBonus"));
        enchantPowerBonus.setColSpan(ColSpan.HALF);

        flammability = new IntegerField(0, Integer.MAX_VALUE, 0);
        flammability.setLabel(AppL10n.localize("block.extra.flammability"));
        flammability.setColSpan(ColSpan.HALF);

        fireSpreadSpeed = new IntegerField(0, Integer.MAX_VALUE, 0);
        fireSpreadSpeed.setLabel(AppL10n.localize("block.extra.fireSpreadSpeed"));
        fireSpreadSpeed.setColSpan(ColSpan.HALF);

        pushReaction = new ChoiceBoxField<>();
        pushReaction.setLabel(AppL10n.localize("block.extra.pushReaction"));
        pushReaction.setConverter(LocalizableConverter.instance());
        pushReaction.getItems().addAll(PushReaction.values());
        pushReaction.setColSpan(ColSpan.HALF);

        aiPathNodeType = new ChoiceBoxField<>();
        aiPathNodeType.setLabel(AppL10n.localize("block.extra.aiPathNodeType"));
        aiPathNodeType.setConverter(LocalizableConverter.instance());
        aiPathNodeType.getItems().addAll(PathNodeType.values());
        aiPathNodeType.setColSpan(ColSpan.HALF);

        Section extra = new Section();
        extra.setText(AppL10n.localize("block.extra.title"));
        extra.getElements().addAll(
                doNotRegisterItem, mapColor,
                beaconColor, beaconBase,
                climbable, replaceable,
                canConnectRedstone, redstonePower,
                canPlantPlant, enchantPowerBonus,
                flammability, fireSpreadSpeed,
                pushReaction, aiPathNodeType
        );

        form.getGroups().addAll(properties, appearance, collision, extra);

        return new FormView(form);
    }

    @Override
    protected void initialize(MEBlock element) {
        identifier.setValue(element.getIdentifier());
        displayName.setValue(element.getDisplayName());
        type.setValue(element.getType());
        material.setValue(element.getMaterial());
        itemGroup.setValue(element.getItemGroup());
        soundType.setValue(element.getSoundType());
        hardness.setValue(element.getHardness());
        unbreakable.set(element.isUnbreakable());
        resistance.setValue(element.getResistance());
        slipperiness.setValue(element.getSlipperiness());
        brightness.setValue(element.getBrightness());
        opacity.setValue(element.getOpacity());
        noCollision.set(element.isNoCollision());
        replaceable.set(element.isReplaceable());
        harvestTool.setValue(element.getHarvestTool());
        harvestLevel.setValue(element.getHarvestLevel());
        final String[] array = element.getInformation();
        information.setValue(StringUtils.join(array, System.lineSeparator()));

        model.setModel(element.getModel());
        model.setCustomModels(element.getCustomModels());
        textures.setTextures(element.getTextures());
        particleTexture.setTexture(element.getParticleTexture());
        transparency.set(element.isTransparency());
        renderType.setValue(element.getRenderType());
        offsetType.setValue(element.getOffsetType());
        itemModel.setModel(element.getItemModel());
        itemModel.setCustomModels(element.getCustomItemModels());
        itemTextures.setTextures(element.getItemTextures());

        BoundingBox boundingBox = element.getBoundingBox();
        minX.setValue(boundingBox.minX() * 16);
        minY.setValue(boundingBox.minY() * 16);
        minZ.setValue(boundingBox.minZ() * 16);
        maxX.setValue(boundingBox.maxX() * 16);
        maxY.setValue(boundingBox.maxY() * 16);
        maxZ.setValue(boundingBox.maxZ() * 16);

        doNotRegisterItem.set(element.isDoNotRegisterItem());
        mapColor.setValue(element.getMapColor());
        beaconColor.setValue(Color.valueOf(element.getBeaconColor()));
        beaconBase.set(element.isBeaconBase());
        climbable.set(element.isClimbable());
        canConnectRedstone.set(element.isCanConnectRedstone());
        redstonePower.setValue(element.getRedstonePower());
        canPlantPlant.setValue(element.getCanPlantPlant());
        enchantPowerBonus.setValue(element.getEnchantPowerBonus());
        flammability.setValue(element.getFlammability());
        fireSpreadSpeed.setValue(element.getFireSpreadSpeed());
        pushReaction.setValue(element.getPushReaction());
        aiPathNodeType.setValue(element.getAiPathNodeType());
    }

    @Override
    protected void updateDataModel(MEBlock element) {
        element.setIdentifier(identifier.getValue().trim());
        element.setDisplayName(displayName.getValue());
        element.setType(type.getValue());
        element.setMaterial(material.getValue());
        element.setItemGroup(itemGroup.getValue());
        element.setSoundType(soundType.getValue());
        element.setHardness(hardness.getValue());
        element.setUnbreakable(unbreakable.get());
        element.setResistance(resistance.getValue());
        element.setSlipperiness(slipperiness.getValue());
        element.setBrightness(brightness.getValue());
        element.setOpacity(opacity.getValue());
        element.setNoCollision(noCollision.get());
        element.setReplaceable(replaceable.get());
        element.setHarvestTool(harvestTool.getValue());
        element.setHarvestLevel(harvestLevel.getValue());
        final String str = information.getValue();
        element.setInformation(StringUtils.splitByLineSeparator(str));

        element.setModel(model.getModel());
        element.setCustomModels(model.getCustomModels());
        element.setTextures(textures.getTextures());
        element.setParticleTexture(particleTexture.getTexture());
        element.setTransparency(transparency.get());
        element.setRenderType(renderType.getValue());
        element.setOffsetType(offsetType.getValue());
        element.setItemModel(itemModel.getModel());
        element.setCustomItemModels(itemModel.getCustomModels());
        element.setItemTextures(itemTextures.getTextures());

        element.setBoundingBox(new BoundingBox(
                minX.getValue() / 16, minY.getValue() / 16, minZ.getValue() / 16,
                maxX.getValue() / 16, maxY.getValue() / 16, maxZ.getValue() / 16));

        element.setDoNotRegisterItem(doNotRegisterItem.get());
        element.setMapColor(mapColor.getValue());
        element.setBeaconColor(beaconColor.getValue().toString());
        element.setBeaconBase(beaconBase.get());
        element.setClimbable(climbable.get());
        element.setCanConnectRedstone(canConnectRedstone.get());
        element.setRedstonePower(redstonePower.getValue());
        element.setCanPlantPlant(canPlantPlant.getValue());
        element.setEnchantPowerBonus(enchantPowerBonus.getValue());
        element.setFlammability(flammability.getValue());
        element.setFireSpreadSpeed(fireSpreadSpeed.getValue());
        element.setPushReaction(pushReaction.getValue());
        element.setAiPathNodeType(aiPathNodeType.getValue());
    }

    @Override
    protected boolean validate() {
        return form.validate();
    }
}
