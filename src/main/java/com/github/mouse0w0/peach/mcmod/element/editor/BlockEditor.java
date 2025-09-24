package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.mcmod.BlockType;
import com.github.mouse0w0.peach.mcmod.BoundingBox;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.element.impl.BlockElement;
import com.github.mouse0w0.peach.mcmod.index.IndexKeys;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.ui.GameDataConverter;
import com.github.mouse0w0.peach.mcmod.ui.LocalizableConverter;
import com.github.mouse0w0.peach.mcmod.ui.cell.IconicDataCell;
import com.github.mouse0w0.peach.mcmod.ui.control.ModelPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.TexturePicker;
import com.github.mouse0w0.peach.mcmod.util.IdentifierUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.control.DoubleSpinner;
import com.github.mouse0w0.peach.ui.control.IntegerSpinner;
import com.github.mouse0w0.peach.ui.util.Validator;
import com.github.mouse0w0.peach.util.StringUtils;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import static com.github.mouse0w0.peach.l10n.AppL10n.localize;
import static com.github.mouse0w0.peach.ui.layout.Form.form;
import static com.github.mouse0w0.peach.ui.layout.FormItem.half;
import static com.github.mouse0w0.peach.ui.layout.FormItem.one;
import static com.github.mouse0w0.peach.ui.layout.LayoutUtils.scrollVBox;
import static com.github.mouse0w0.peach.ui.layout.LayoutUtils.titled;

public class BlockEditor extends ElementEditor<BlockElement> {
    private TextField identifier;
    private TextField displayName;
    private ComboBox<BlockType> type;
    private ComboBox<String> itemGroup;
    private ComboBox<String> material;
    private ComboBox<String> soundType;
    private DoubleSpinner hardness;
    private RadioButton unbreakable;
    private DoubleSpinner resistance;
    private DoubleSpinner slipperiness;
    private IntegerSpinner brightness;
    private IntegerSpinner opacity;
    private ComboBox<String> harvestTool;
    private IntegerSpinner harvestLevel;
    private RadioButton replaceable;
    private TextArea information;

    private DoubleSpinner minX;
    private DoubleSpinner minY;
    private DoubleSpinner minZ;
    private DoubleSpinner maxX;
    private DoubleSpinner maxY;
    private DoubleSpinner maxZ;
    private RadioButton noCollision;

    private ModelPicker model;
    private TexturePicker particleTexture;
    private RadioButton transparency;
    private ComboBox<String> renderType;
    private ComboBox<String> offsetType;
    private ModelPicker itemModel;

    //    private Object dropItem; // TODO

    private RadioButton doNotRegisterItem;
    private ComboBox<String> mapColor;
    private ColorPicker beaconColor;
    private RadioButton beaconBase;
    private RadioButton climbable;
    private RadioButton canConnectRedstone;
    private IntegerSpinner redstonePower;
    private ComboBox<String> canPlantPlant;
    private DoubleSpinner enchantPowerBonus;
    private IntegerSpinner flammability;
    private IntegerSpinner fireSpreadSpeed;
    private ComboBox<String> pushReaction;
    private ComboBox<String> aiPathNodeType;
    //    private Object pickItem; // TODO

    public BlockEditor(@NotNull Project project, @NotNull BlockElement element) {
        super(project, element);
    }

    @Override
    protected Node getContent() {
        var indexManager = IndexManager.getInstance(getProject());

        identifier = new TextField();
        Validator.of(identifier, localize("block.properties.identifier"), IdentifierUtils::validateIdentifier);

        displayName = new TextField();

        type = new ComboBox<>();
        type.setConverter(LocalizableConverter.instance());
        type.getItems().addAll(BlockType.VALUES);
        type.setValue(BlockType.NORMAL);

        var itemGroupIndex = indexManager.getIndex(IndexKeys.ITEM_GROUP);
        itemGroup = new ComboBox<>();
        itemGroup.setCellFactory(IconicDataCell.factory(getProject(), itemGroupIndex));
        itemGroup.setButtonCell(IconicDataCell.create(getProject(), itemGroupIndex));
        itemGroup.setItems(itemGroupIndex.keyList());

        var materialIndex = indexManager.getIndex(IndexKeys.MATERIAL);
        material = new ComboBox<>();
        material.setCellFactory(IconicDataCell.factory(getProject(), materialIndex));
        material.setButtonCell(IconicDataCell.create(getProject(), materialIndex));
        material.setItems(materialIndex.keyList());

        var soundTypeIndex = indexManager.getIndex(IndexKeys.SOUND_TYPE);
        soundType = new ComboBox<>();
        soundType.setCellFactory(IconicDataCell.factory(getProject(), soundTypeIndex));
        soundType.setButtonCell(IconicDataCell.create(getProject(), soundTypeIndex));
        soundType.setItems(soundTypeIndex.keyList());

        hardness = new DoubleSpinner(0D, Double.MAX_VALUE, 0D);

        unbreakable = new RadioButton();
        hardness.disableProperty().bind(unbreakable.selectedProperty());

        resistance = new DoubleSpinner(0D, Double.MAX_VALUE, 0D);

        slipperiness = new DoubleSpinner(0D, 1D, 0.6D);

        brightness = new IntegerSpinner(0, 15, 0);

        opacity = new IntegerSpinner(0, 255, 255);

        var toolTypeIndex = indexManager.getIndex(IndexKeys.TOOL_TYPE);
        harvestTool = new ComboBox<>();
        harvestTool.setConverter(GameDataConverter.create(toolTypeIndex));
        harvestTool.setItems(toolTypeIndex.keyList());

        harvestLevel = new IntegerSpinner(0, Integer.MAX_VALUE, 0);
        harvestLevel.disableProperty().bind(harvestTool.valueProperty().isEqualTo("NONE"));

        information = new TextArea();

        var properties = titled(localize("block.properties.title"), form(
                half(localize("block.properties.identifier"), identifier),
                half(localize("block.properties.displayName"), displayName),
                half(localize("block.properties.type"), type),
                half(localize("block.properties.itemGroup"), itemGroup),
                half(localize("block.properties.material"), material),
                half(localize("block.properties.soundType"), soundType),
                half(localize("block.properties.hardness"), hardness),
                half(localize("block.properties.unbreakable"), unbreakable),
                half(localize("block.properties.resistance"), resistance),
                half(localize("block.properties.slipperiness"), slipperiness),
                half(localize("block.properties.brightness"), brightness),
                half(localize("block.properties.opacity"), opacity),
                half(localize("block.properties.harvestTool"), harvestTool),
                half(localize("block.properties.harvestLevel"), harvestLevel),
                one(localize("block.properties.information"), information)
        ));

        model = new ModelPicker(
                getProject(),
                localize("block.appearance.model"),
                new ResourceStore(
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.MODELS),
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.BLOCK_MODELS), ".json"),
                localize("block.appearance.texture"),
                new ResourceStore(
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.BLOCK_TEXTURES), ".png")
        );
        model.blockstateProperty().bind(Bindings.createStringBinding(() -> type.getValue().getBlockstate(), type.valueProperty()));

        particleTexture = new TexturePicker(new ResourceStore(
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(getProject(), ResourceUtils.BLOCK_TEXTURES), ".png"));
        particleTexture.setFitSize(64, 64);
        particleTexture.setMaxSize(64, 64);

        transparency = new RadioButton();
        transparency.selectedProperty().addListener(observable -> opacity.setValue(transparency.isSelected() ? 0 : 255));

        var renderTypeIndex = indexManager.getIndex(IndexKeys.RENDER_TYPE);
        renderType = new ComboBox<>();
        renderType.setConverter(GameDataConverter.create(renderTypeIndex));
        renderType.setItems(renderTypeIndex.keyList());

        var offsetTypeIndex = indexManager.getIndex(IndexKeys.OFFSET_TYPE);
        offsetType = new ComboBox<>();
        offsetType.setConverter(GameDataConverter.create(offsetTypeIndex));
        offsetType.setItems(offsetTypeIndex.keyList());

        itemModel = new ModelPicker(getProject(),
                localize("block.appearance.itemModel"),
                new ResourceStore(
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.MODELS),
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_MODELS), ".json"),
                localize("block.appearance.itemTexture"),
                new ResourceStore(
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.TEXTURES),
                        ResourceUtils.getResourcePath(getProject(), ResourceUtils.ITEM_TEXTURES), ".png")
        );
        itemModel.setBlockstate("item");
        var modelManager = ModelManager.getInstance(getProject());
        itemModel.defaultModelEnabledProperty().bind(Bindings.createBooleanBinding(() -> {
            if (modelManager.getBlockstateTemplate(type.getValue().getBlockstate()).getItem() != null) return true;
            Identifier blockModel = model.getModel();
            if (blockModel == null) return false;
            if (ModelManager.CUSTOM.equals(blockModel)) return false;
            return modelManager.getModelTemplate(blockModel).getItem() != null;
        }, type.valueProperty(), model.modelProperty()));

        var appearance = titled(localize("block.appearance.title"), form(
                one(model),
                one(localize("block.appearance.particleTexture"), particleTexture),
                half(localize("block.appearance.transparency"), transparency),
                half(localize("block.appearance.renderType"), renderType),
                half(localize("block.appearance.offsetType"), offsetType),
                one(itemModel)
        ));

        minX = new DoubleSpinner(0D, 16D, 0D);

        minY = new DoubleSpinner(0D, 24D, 0D);

        minZ = new DoubleSpinner(0D, 16D, 0D);

        maxX = new DoubleSpinner(0D, 16D, 16D);

        maxY = new DoubleSpinner(0D, 24D, 16D);

        maxZ = new DoubleSpinner(0D, 16D, 16D);

        noCollision = new RadioButton();

        var collision = titled(localize("block.collision.title"), form(
                half(localize("block.collision.minX"), minX),
                half(localize("block.collision.minY"), minY),
                half(localize("block.collision.minZ"), minZ),
                half(localize("block.collision.maxX"), maxX),
                half(localize("block.collision.maxY"), maxY),
                half(localize("block.collision.maxZ"), maxZ),
                half(localize("block.collision.noCollision"), noCollision)
        ));

        doNotRegisterItem = new RadioButton();

        var mapColorIndex = indexManager.getIndex(IndexKeys.MAP_COLOR);
        mapColor = new ComboBox<>();
        mapColor.setCellFactory(IconicDataCell.factory(getProject(), mapColorIndex));
        mapColor.setButtonCell(IconicDataCell.create(getProject(), mapColorIndex));
        mapColor.setItems(mapColorIndex.keyList());

        beaconColor = new ColorPicker();

        beaconBase = new RadioButton();

        climbable = new RadioButton();

        replaceable = new RadioButton();

        canConnectRedstone = new RadioButton();

        redstonePower = new IntegerSpinner(0, 15, 0);

        var plantTypeIndex = indexManager.getIndex(IndexKeys.PLANT_TYPE);
        canPlantPlant = new ComboBox<>();
        canPlantPlant.setConverter(GameDataConverter.create(plantTypeIndex));
        canPlantPlant.setItems(plantTypeIndex.keyList());

        enchantPowerBonus = new DoubleSpinner(0, Double.MAX_VALUE, 0);

        flammability = new IntegerSpinner(0, Integer.MAX_VALUE, 0);

        fireSpreadSpeed = new IntegerSpinner(0, Integer.MAX_VALUE, 0);

        var pushReactionIndex = indexManager.getIndex(IndexKeys.PUSH_REACTION);
        pushReaction = new ComboBox<>();
        pushReaction.setConverter(GameDataConverter.create(pushReactionIndex));
        pushReaction.setItems(pushReactionIndex.keyList());

        var aiPathNodeTypeIndex = indexManager.getIndex(IndexKeys.AI_PATH_NODE_TYPE);
        aiPathNodeType = new ComboBox<>();
        aiPathNodeType.setConverter(GameDataConverter.create(aiPathNodeTypeIndex));
        aiPathNodeType.setItems(aiPathNodeTypeIndex.keyList());

        var extra = titled(localize("block.extra.title"), form(
                half(localize("block.extra.doNotRegisterItem"), doNotRegisterItem),
                half(localize("block.extra.mapColor"), mapColor),
                half(localize("block.extra.beaconColor"), beaconColor),
                half(localize("block.extra.beaconBase"), beaconBase),
                half(localize("block.extra.climbable"), climbable),
                half(localize("block.extra.replaceable"), replaceable),
                half(localize("block.extra.canConnectRedstone"), canConnectRedstone),
                half(localize("block.extra.redstonePower"), redstonePower),
                half(localize("block.extra.canPlantPlant"), canPlantPlant),
                half(localize("block.extra.enchantPowerBonus"), enchantPowerBonus),
                half(localize("block.extra.flammability"), flammability),
                half(localize("block.extra.fireSpreadSpeed"), fireSpreadSpeed),
                half(localize("block.extra.pushReaction"), pushReaction),
                half(localize("block.extra.aiPathNodeType"), aiPathNodeType)
        ));


        return scrollVBox(properties, appearance, collision, extra);
    }

    @Override
    protected void initialize(BlockElement element) {
        identifier.setText(element.getIdentifier());
        displayName.setText(element.getDisplayName());
        type.setValue(element.getType());
        itemGroup.setValue(element.getItemGroup());
        material.setValue(element.getMaterial());
        soundType.setValue(element.getSoundType());
        hardness.setValue(element.getHardness());
        unbreakable.setSelected(element.isUnbreakable());
        resistance.setValue(element.getResistance());
        slipperiness.setValue(element.getSlipperiness());
        brightness.setValue(element.getBrightness());
        opacity.setValue(element.getOpacity());
        noCollision.setSelected(element.isNoCollision());
        replaceable.setSelected(element.isReplaceable());
        harvestTool.setValue(element.getHarvestTool());
        harvestLevel.setValue(element.getHarvestLevel());
        information.setText(StringUtils.join(element.getInformation(), '\n'));

        model.setModel(element.getModel());
        model.setCustomModels(element.getCustomModels());
        model.setTextures(element.getTextures());
        particleTexture.setResource(element.getParticleTexture());
        transparency.setSelected(element.isTransparency());
        renderType.setValue(element.getRenderType());
        offsetType.setValue(element.getOffsetType());
        itemModel.setModel(element.getItemModel());
        itemModel.setCustomModels(element.getCustomItemModels());
        itemModel.setTextures(element.getItemTextures());

        BoundingBox boundingBox = element.getBoundingBox();
        minX.setValue(boundingBox.minX() * 16);
        minY.setValue(boundingBox.minY() * 16);
        minZ.setValue(boundingBox.minZ() * 16);
        maxX.setValue(boundingBox.maxX() * 16);
        maxY.setValue(boundingBox.maxY() * 16);
        maxZ.setValue(boundingBox.maxZ() * 16);

        doNotRegisterItem.setSelected(element.isDoNotRegisterItem());
        mapColor.setValue(element.getMapColor());
        beaconColor.setValue(Color.valueOf(element.getBeaconColor()));
        beaconBase.setSelected(element.isBeaconBase());
        climbable.setSelected(element.isClimbable());
        canConnectRedstone.setSelected(element.isCanConnectRedstone());
        redstonePower.setValue(element.getRedstonePower());
        canPlantPlant.setValue(element.getCanPlantPlant());
        enchantPowerBonus.setValue(element.getEnchantPowerBonus());
        flammability.setValue(element.getFlammability());
        fireSpreadSpeed.setValue(element.getFireSpreadSpeed());
        pushReaction.setValue(element.getPushReaction());
        aiPathNodeType.setValue(element.getAiPathNodeType());
    }

    @Override
    protected void updateDataModel(BlockElement element) {
        element.setIdentifier(identifier.getText());
        element.setDisplayName(displayName.getText());
        element.setType(type.getValue());
        element.setItemGroup(itemGroup.getValue());
        element.setMaterial(material.getValue());
        element.setSoundType(soundType.getValue());
        element.setHardness(hardness.getValue());
        element.setUnbreakable(unbreakable.isSelected());
        element.setResistance(resistance.getValue());
        element.setSlipperiness(slipperiness.getValue());
        element.setBrightness(brightness.getValue());
        element.setOpacity(opacity.getValue());
        element.setNoCollision(noCollision.isSelected());
        element.setReplaceable(replaceable.isSelected());
        element.setHarvestTool(harvestTool.getValue());
        element.setHarvestLevel(harvestLevel.getValue());
        element.setInformation(StringUtils.splitByLineSeparator(information.getText()));

        element.setModel(model.getModel());
        element.setCustomModels(model.getCustomModels());
        element.setTextures(model.getTextures());
        element.setParticleTexture(particleTexture.getResource());
        element.setTransparency(transparency.isSelected());
        element.setRenderType(renderType.getValue());
        element.setOffsetType(offsetType.getValue());
        element.setItemModel(itemModel.getModel());
        element.setCustomItemModels(itemModel.getCustomModels());
        element.setItemTextures(itemModel.getTextures());

        element.setBoundingBox(new BoundingBox(
                minX.getValue() / 16, minY.getValue() / 16, minZ.getValue() / 16,
                maxX.getValue() / 16, maxY.getValue() / 16, maxZ.getValue() / 16));

        element.setDoNotRegisterItem(doNotRegisterItem.isSelected());
        element.setMapColor(mapColor.getValue());
        element.setBeaconColor(beaconColor.getValue().toString());
        element.setBeaconBase(beaconBase.isSelected());
        element.setClimbable(climbable.isSelected());
        element.setCanConnectRedstone(canConnectRedstone.isSelected());
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
        return Validator.validate(identifier);
    }
}
