package com.github.mouse0w0.peach.mcmod.generator.v1_12_2;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.ElementTypes;
import com.github.mouse0w0.peach.mcmod.element.impl.MEBlock;
import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.mcmod.generator.Filer;
import com.github.mouse0w0.peach.mcmod.generator.task.Task;
import com.github.mouse0w0.peach.mcmod.generator.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.generator.util.ModelUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.BlockClassGenerator;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.BlockLoaderClassGenerator;
import com.github.mouse0w0.peach.mcmod.model.Blockstate;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import javafx.scene.paint.Color;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GenBlock implements Task {

    @Override
    public void run(Context context) throws Exception {
        BlockLoaderClassGenerator loader = new BlockLoaderClassGenerator(context.getInternalName("block/BlockLoader"), context.getNamespace());
        String classItemGroups = context.getInternalName("init/ItemGroups");

        for (MEBlock block : context.getElements(ElementTypes.BLOCK)) {
            String namespace = context.getNamespace();
            String identifier = block.getIdentifier();

            BlockClassGenerator cg = new BlockClassGenerator(context.getInternalName("block/Block" + JavaUtils.lowerUnderscoreToUpperCamel(identifier)));
            BlockType type = block.getType();
            if (type == BlockType.NORMAL) {
                cg.visitBlock("net/minecraft/block/Block", block.getMaterial().getId(), block.getMapColor().getId());
            }

            cg.visitIdentifier(identifier);
            cg.visitTranslationKey(namespace + "." + identifier);
            cg.visitItemGroup(classItemGroups, block.getItemGroup().getId());
            cg.visitSoundType(block.getSoundType().getId());
            if (block.isUnbreakable()) cg.visitHardness(-1);
            else if (block.getHardness() != 0) cg.visitHardness((float) block.getHardness());
            if (block.getResistance() != 0) cg.visitResistance((float) block.getResistance());
            if (block.getSlipperiness() != 0.6) cg.visitSlipperiness((float) block.getSlipperiness());
            if (block.getBrightness() != 0) cg.visitBrightness(block.getBrightness());
            if (block.isTransparency() ? block.getOpacity() != 0 : block.getOpacity() != 255)
                cg.visitOpacity(block.getOpacity());
            if (!ToolType.NONE.equals(block.getHarvestTool()))
                cg.visitHarvestToolAndLevel(block.getHarvestTool(), block.getHarvestLevel());

            if (block.isTransparency()) cg.visitTransparency();
            if (block.getRenderType() != RenderType.SOLID) cg.visitRenderType(block.getRenderType());
            if (block.getOffsetType() != OffsetType.NONE) cg.visitOffsetType(block.getOffsetType());

            if (block.isNoCollision() || !MEBlock.FULL_BLOCK.equals(block.getBoundingBox())) cg.visitNoFullCube();
            if (block.isNoCollision()) cg.visitNoCollision();
            if (!MEBlock.FULL_BLOCK.equals(block.getBoundingBox())) cg.visitBoundingBox(block.getBoundingBox());

            Color beaconColor = Color.valueOf(block.getBeaconColor());
            if (beaconColor.isOpaque()) cg.visitBeaconColor(beaconColor);
            if (block.isBeaconBase()) cg.visitBeaconBase();
            if (block.isClimbable()) cg.visitClimbable();
            if (block.isReplaceable()) cg.visitReplaceable();
            if (block.isCanConnectRedstone()) cg.visitCanConnectRedstone();
            if (block.getRedstonePower() != 0) cg.visitRedstonePower(block.getRedstonePower());
            if (block.getCanPlantPlant() != PlantType.NONE) cg.visitCanPlantPlant(block.getCanPlantPlant());
            if (block.getEnchantPowerBonus() != 0) cg.visitEnchantPowerBonus((float) block.getEnchantPowerBonus());
            if (block.getFlammability() != 0) cg.visitFlammability(block.getFlammability());
            if (block.getFireSpreadSpeed() != 0) cg.visitFireSpreadSpeed(block.getFireSpreadSpeed());
            if (block.getPushReaction() != PushReaction.INHERIT) cg.visitPushReaction(block.getPushReaction());
            if (block.getAiPathNodeType() != PathNodeType.INHERIT) cg.visitAiPathNodeType(block.getAiPathNodeType());
            context.getClassesFiler().write(cg.getThisName() + ".class", cg.toByteArray());

            loader.visitBlock(identifier, cg.getThisName());

            // Copy textures
            Filer assetsFiler = context.getAssetsFiler();

            Path texturesPath = context.getProjectStructure().getTextures();
            for (String texture : block.getTextures().values()) {
                assetsFiler.copy(texturesPath.resolve(texture + ".png"), "textures/" + texture + ".png");
            }

            for (String texture : block.getItemTextures().values()) {
                assetsFiler.copy(texturesPath.resolve(texture + ".png"), "textures/" + texture + ".png");
            }
            // Generate models
            ModelManager modelManager = context.getModelManager();
            Blockstate blockstate = modelManager.getBlockstate(block.getType().getBlockstate());
            Identifier model = block.getModelPrototype();
            Map<String, String> outputModels = new HashMap<>();

            Map<String, String> textures = ModelUtils.processTextures(namespace, block.getTextures());
            String particleTexture = block.getParticleTexture() != null ?
                    ModelUtils.processResourcePath(namespace, block.getParticleTexture()) : null;
            if (ModelManager.CUSTOM.equals(model)) {
                ModelUtils.generateCustomModel(namespace, identifier, blockstate, block.getModels(), context.getProjectStructure().getModels(),
                        textures, particleTexture, assetsFiler.getRoot(), outputModels);
            } else {
                ModelUtils.generateModel(namespace, identifier, blockstate, modelManager.getModelPrototype(model),
                        textures, particleTexture, assetsFiler.getRoot(), outputModels);
            }

            Identifier itemModel = block.getItemModelPrototype();
            Map<String, String> itemTextures = ModelUtils.processTextures(namespace, block.getItemTextures());
            if (ModelManager.CUSTOM.equals(itemModel)) {
                ModelUtils.generateCustomModel(namespace, identifier, blockstate, block.getModels(), context.getProjectStructure().getModels(),
                        itemTextures, null, assetsFiler.getRoot(), outputModels);
            } else if (ModelManager.INHERIT.equals(itemModel)) {
                ModelUtils.generateModel(namespace, identifier, blockstate, modelManager.getModelPrototype(blockstate.getItem()),
                        itemTextures, null, assetsFiler.getRoot(), outputModels);
            } else {
                ModelUtils.generateModel(namespace, identifier, blockstate, modelManager.getModelPrototype(itemModel),
                        itemTextures, null, assetsFiler.getRoot(), outputModels);
            }

            // Generate Blockstate
            Configuration templateManager = context.getTemplateManager();
            Template template = templateManager.getTemplate(blockstate.getTemplate());
            template.process(ModelUtils.processBlockstateModels(namespace, outputModels), assetsFiler.newWriter("blockstates/" + identifier + ".json"));
        }

        context.getClassesFiler().write(loader.getThisName() + ".class", loader.toByteArray());
    }
}
