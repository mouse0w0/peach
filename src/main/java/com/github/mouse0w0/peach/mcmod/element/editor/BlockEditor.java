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
    private SpinnerField<Double> hardness;
    private RadioButtonField unbreakable;
    private SpinnerField<Double> resistance;
    private SpinnerField<Double> slipperiness;
    private SpinnerField<Integer> brightness;
    private SpinnerField<Integer> opacity;
    private ChoiceBoxField<String> harvestTool;
    private SpinnerField<Integer> harvestLevel;
    private RadioButtonField replaceable;
    private TextAreaField information;

    private SpinnerField<Double> minX;
    private SpinnerField<Double> minY;
    private SpinnerField<Double> minZ;
    private SpinnerField<Double> maxX;
    private SpinnerField<Double> maxY;
    private SpinnerField<Double> maxZ;
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
    private SpinnerField<Integer> redstonePower;
    private ChoiceBoxField<PlantType> canPlantPlant;
    private SpinnerField<Double> enchantPowerBonus;
    private SpinnerField<Integer> flammability;
    private SpinnerField<Integer> fireSpreadSpeed;
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
        identifier.getChecks().add(new Check<>(ModUtils::validateIdentifier, NotificationLevel.ERROR, I18n.translate("validate.illegalIdentifier")));
        identifier.setText(I18n.translate("block.properties.identifier"));
        identifier.setColSpan(ColSpan.HALF);

        displayName = new TextFieldField();
        displayName.setText(I18n.translate("block.properties.displayName"));
        displayName.setColSpan(ColSpan.HALF);

        type = new ChoiceBoxField<>();
        type.setText(I18n.translate("block.properties.type"));
        type.setConverter(LocalizableConverter.instance());
        type.getItems().addAll(BlockType.values());
        type.setValue(BlockType.NORMAL);
        type.setColSpan(ColSpan.HALF);

        material = new ComboBoxField<>();
        material.setText(I18n.translate("block.properties.material"));
        material.setCellFactory(LocalizableWithItemIconCell.factory());
        material.setButtonCell(LocalizableWithItemIconCell.create());
        material.getItems().addAll(indexManager.getIndex(Indexes.MATERIALS).values());
        material.setColSpan(ColSpan.HALF);

        itemGroup = new ComboBoxField<>();
        itemGroup.setText(I18n.translate("block.properties.itemGroup"));
        itemGroup.setCellFactory(LocalizableWithItemIconCell.factory());
        itemGroup.setButtonCell(LocalizableWithItemIconCell.create());
        itemGroup.getItems().addAll(indexManager.getIndex(Indexes.ITEM_GROUPS).values());
        itemGroup.setColSpan(ColSpan.HALF);

        soundType = new ComboBoxField<>();
        soundType.setText(I18n.translate("block.properties.soundType"));
        soundType.setCellFactory(LocalizableWithItemIconCell.factory());
        soundType.setButtonCell(LocalizableWithItemIconCell.create());
        soundType.getItems().addAll(indexManager.getIndex(Indexes.SOUND_TYPES).values());
        soundType.setColSpan(ColSpan.HALF);

        hardness = new SpinnerField<>(0D, Double.MAX_VALUE, 0D);
        hardness.setText(I18n.translate("block.properties.hardness"));
        hardness.setColSpan(ColSpan.HALF);

        unbreakable = new RadioButtonField();
        unbreakable.setText(I18n.translate("block.properties.unbreakable"));
        unbreakable.setColSpan(ColSpan.HALF);
        hardness.disableProperty().bind(unbreakable.valueProperty());

        resistance = new SpinnerField<>(0D, Double.MAX_VALUE, 0D);
        resistance.setText(I18n.translate("block.properties.resistance"));
        resistance.setColSpan(ColSpan.HALF);

        slipperiness = new SpinnerField<>(0D, 1D, 0.6D);
        slipperiness.setText(I18n.translate("block.properties.slipperiness"));
        slipperiness.setColSpan(ColSpan.HALF);

        brightness = new SpinnerField<>(0, 15, 0);
        brightness.setText(I18n.translate("block.properties.brightness"));
        brightness.setColSpan(ColSpan.HALF);

        opacity = new SpinnerField<>(0, 255, 255);
        opacity.setText(I18n.translate("block.properties.opacity"));
        opacity.setColSpan(ColSpan.HALF);

        harvestTool = new ChoiceBoxField<>();
        harvestTool.setText(I18n.translate("block.properties.harvestTool"));
        harvestTool.setColSpan(ColSpan.HALF);
        harvestTool.setConverter(new StringConverter<>() {
            @Override
            public String toString(String object) {
                return I18n.translate("toolType." + object);
            }

            @Override
            public String fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        harvestTool.getItems().add(ToolType.NONE);
        harvestTool.getItems().addAll(ToolType.getToolTypes());

        harvestLevel = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        harvestLevel.setText(I18n.translate("block.properties.harvestLevel"));
        harvestLevel.setColSpan(ColSpan.HALF);
        harvestLevel.disableProperty().bind(harvestTool.valueProperty().isEqualTo(ToolType.NONE));

        information = new TextAreaField();
        information.setText(I18n.translate("block.properties.information"));

        Section properties = new Section();
        properties.setText(I18n.translate("block.properties.title"));
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
        model.setText(I18n.translate("block.appearance.model"));
        model.blockstateProperty().bind(Bindings.createStringBinding(() -> type.getValue().getBlockstate(), type.valueProperty()));

        textures = new ModelTextureField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.BLOCK_TEXTURES), ".png"));
        textures.setText(I18n.translate("block.appearance.texture"));
        model.getTextures().addListener((InvalidationListener) observable -> textures.setTextureKeys(model.getTextures()));

        particleTexture = new TextureField(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.BLOCK_TEXTURES), ".png"));
        particleTexture.setText(I18n.translate("block.appearance.particleTexture"));
        particleTexture.setFitSize(64, 64);

        transparency = new RadioButtonField();
        transparency.setText(I18n.translate("block.appearance.transparency"));
        transparency.setColSpan(ColSpan.HALF);
        transparency.valueProperty().addListener(observable -> opacity.setValue(transparency.getValue() ? 0 : 255));

        renderType = new ChoiceBoxField<>();
        renderType.setText(I18n.translate("block.appearance.renderType"));
        renderType.setConverter(LocalizableConverter.instance());
        renderType.getItems().addAll(RenderType.values());
        renderType.setColSpan(ColSpan.HALF);

        offsetType = new ChoiceBoxField<>();
        offsetType.setText(I18n.translate("block.appearance.offsetType"));
        offsetType.setConverter(LocalizableConverter.instance());
        offsetType.getItems().addAll(OffsetType.values());
        offsetType.setColSpan(ColSpan.HALF);

        itemModel = new ModelField(getProject(), new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.MODELS),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_MODELS), ".json"));
        itemModel.setText(I18n.translate("block.appearance.itemModel"));
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
        itemTextures.setText(I18n.translate("block.appearance.itemTexture"));
        itemTextures.visibleProperty().bind(Bindings.isNotEmpty(itemModel.getTextures()));
        itemModel.getTextures().addListener((InvalidationListener) observable -> itemTextures.setTextureKeys(itemModel.getTextures()));

        Section appearance = new Section();
        appearance.setText(I18n.translate("block.appearance.title"));
        appearance.getElements().addAll(
                model,
                textures,
                particleTexture,
                transparency, renderType,
                offsetType,
                itemModel,
                itemTextures
        );

        minX = new SpinnerField<>(0D, 16D, 0D);
        minX.setText(I18n.translate("block.collision.minX"));
        minX.setColSpan(ColSpan.THIRD);

        minY = new SpinnerField<>(0D, 24D, 0D);
        minY.setText(I18n.translate("block.collision.minY"));
        minY.setColSpan(ColSpan.THIRD);

        minZ = new SpinnerField<>(0D, 16D, 0D);
        minZ.setText(I18n.translate("block.collision.minZ"));
        minZ.setColSpan(ColSpan.THIRD);

        maxX = new SpinnerField<>(0D, 16D, 16D);
        maxX.setText(I18n.translate("block.collision.maxX"));
        maxX.setColSpan(ColSpan.THIRD);

        maxY = new SpinnerField<>(0D, 24D, 16D);
        maxY.setText(I18n.translate("block.collision.maxY"));
        maxY.setColSpan(ColSpan.THIRD);

        maxZ = new SpinnerField<>(0D, 16D, 16D);
        maxZ.setText(I18n.translate("block.collision.maxZ"));
        maxZ.setColSpan(ColSpan.THIRD);

        noCollision = new RadioButtonField();
        noCollision.setText(I18n.translate("block.collision.noCollision"));
        noCollision.setColSpan(ColSpan.HALF);

        Section collision = new Section();
        collision.setText(I18n.translate("block.collision.title"));
        collision.getElements().addAll(
                minX, minY, minZ,
                maxX, maxY, maxZ,
                noCollision);

        doNotRegisterItem = new RadioButtonField();
        doNotRegisterItem.setText(I18n.translate("block.extra.doNotRegisterItem"));
        doNotRegisterItem.setColSpan(ColSpan.HALF);

        mapColor = new ComboBoxField<>();
        mapColor.setText(I18n.translate("block.extra.mapColor"));
        mapColor.setCellFactory(LocalizableWithItemIconCell.factory());
        mapColor.setButtonCell(LocalizableWithItemIconCell.create());
        mapColor.getItems().addAll(indexManager.getIndex(Indexes.MAP_COLORS).values());
        mapColor.setColSpan(ColSpan.HALF);

        beaconColor = new ColorPickerField();
        beaconColor.setText(I18n.translate("block.extra.beaconColor"));
        beaconColor.setColSpan(ColSpan.HALF);

        beaconBase = new RadioButtonField();
        beaconBase.setText(I18n.translate("block.extra.beaconBase"));
        beaconBase.setColSpan(ColSpan.HALF);

        climbable = new RadioButtonField();
        climbable.setText(I18n.translate("block.extra.climbable"));
        climbable.setColSpan(ColSpan.HALF);

        replaceable = new RadioButtonField();
        replaceable.setText(I18n.translate("block.extra.replaceable"));
        replaceable.setColSpan(ColSpan.HALF);

        canConnectRedstone = new RadioButtonField();
        canConnectRedstone.setText(I18n.translate("block.extra.canConnectRedstone"));
        canConnectRedstone.setColSpan(ColSpan.HALF);

        redstonePower = new SpinnerField<>(0, 15, 0);
        redstonePower.setText(I18n.translate("block.extra.redstonePower"));
        redstonePower.setColSpan(ColSpan.HALF);

        canPlantPlant = new ChoiceBoxField<>();
        canPlantPlant.setText(I18n.translate("block.extra.canPlantPlant"));
        canPlantPlant.setConverter(LocalizableConverter.instance());
        canPlantPlant.getItems().addAll(PlantType.values());
        canPlantPlant.setColSpan(ColSpan.HALF);

        enchantPowerBonus = new SpinnerField<>(0, Double.MAX_VALUE, 0);
        enchantPowerBonus.setText(I18n.translate("block.extra.enchantPowerBonus"));
        enchantPowerBonus.setColSpan(ColSpan.HALF);

        flammability = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        flammability.setText(I18n.translate("block.extra.flammability"));
        flammability.setColSpan(ColSpan.HALF);

        fireSpreadSpeed = new SpinnerField<>(0, Integer.MAX_VALUE, 0);
        fireSpreadSpeed.setText(I18n.translate("block.extra.fireSpreadSpeed"));
        fireSpreadSpeed.setColSpan(ColSpan.HALF);

        pushReaction = new ChoiceBoxField<>();
        pushReaction.setText(I18n.translate("block.extra.pushReaction"));
        pushReaction.setConverter(LocalizableConverter.instance());
        pushReaction.getItems().addAll(PushReaction.values());
        pushReaction.setColSpan(ColSpan.HALF);

        aiPathNodeType = new ChoiceBoxField<>();
        aiPathNodeType.setText(I18n.translate("block.extra.aiPathNodeType"));
        aiPathNodeType.setConverter(LocalizableConverter.instance());
        aiPathNodeType.getItems().addAll(PathNodeType.values());
        aiPathNodeType.setColSpan(ColSpan.HALF);

        Section extra = new Section();
        extra.setText(I18n.translate("block.extra.title"));
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
        unbreakable.setValue(element.isUnbreakable());
        resistance.setValue(element.getResistance());
        slipperiness.setValue(element.getSlipperiness());
        brightness.setValue(element.getBrightness());
        opacity.setValue(element.getOpacity());
        noCollision.setValue(element.isNoCollision());
        replaceable.setValue(element.isReplaceable());
        harvestTool.setValue(element.getHarvestTool());
        harvestLevel.setValue(element.getHarvestLevel());
        final String[] array = element.getInformation();
        information.setValue(StringUtils.join(array, System.lineSeparator()));

        model.setModel(element.getModel());
        model.setCustomModels(element.getCustomModels());
        textures.setTextures(element.getTextures());
        particleTexture.setTexture(element.getParticleTexture());
        transparency.setValue(element.isTransparency());
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

        doNotRegisterItem.setValue(element.isDoNotRegisterItem());
        mapColor.setValue(element.getMapColor());
        beaconColor.setValue(Color.valueOf(element.getBeaconColor()));
        beaconBase.setValue(element.isBeaconBase());
        climbable.setValue(element.isClimbable());
        canConnectRedstone.setValue(element.isCanConnectRedstone());
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
        element.setUnbreakable(unbreakable.getValue());
        element.setResistance(resistance.getValue());
        element.setSlipperiness(slipperiness.getValue());
        element.setBrightness(brightness.getValue());
        element.setOpacity(opacity.getValue());
        element.setNoCollision(noCollision.getValue());
        element.setReplaceable(replaceable.getValue());
        element.setHarvestTool(harvestTool.getValue());
        element.setHarvestLevel(harvestLevel.getValue());
        final String str = information.getValue();
        element.setInformation(StringUtils.splitByLineSeparator(str));

        element.setModel(model.getModel());
        element.setCustomModels(model.getCustomModels());
        element.setTextures(textures.getTextures());
        element.setParticleTexture(particleTexture.getTexture());
        element.setTransparency(transparency.getValue());
        element.setRenderType(renderType.getValue());
        element.setOffsetType(offsetType.getValue());
        element.setItemModel(itemModel.getModel());
        element.setCustomItemModels(itemModel.getCustomModels());
        element.setItemTextures(itemTextures.getTextures());

        element.setBoundingBox(new BoundingBox(
                minX.getValue() / 16, minY.getValue() / 16, minZ.getValue() / 16,
                maxX.getValue() / 16, maxY.getValue() / 16, maxZ.getValue() / 16));

        element.setDoNotRegisterItem(doNotRegisterItem.getValue());
        element.setMapColor(mapColor.getValue());
        element.setBeaconColor(beaconColor.getValue().toString());
        element.setBeaconBase(beaconBase.getValue());
        element.setClimbable(climbable.getValue());
        element.setCanConnectRedstone(canConnectRedstone.getValue());
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
