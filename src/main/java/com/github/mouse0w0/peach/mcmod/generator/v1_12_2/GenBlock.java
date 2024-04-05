package com.github.mouse0w0.peach.mcmod.generator.v1_12_2;

import com.github.mouse0w0.peach.mcmod.BlockType;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.element.impl.BlockElement;
import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.mcmod.generator.Filer;
import com.github.mouse0w0.peach.mcmod.generator.task.Task;
import com.github.mouse0w0.peach.mcmod.generator.util.BlockstateUtils;
import com.github.mouse0w0.peach.mcmod.generator.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.generator.util.ModelUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.BlockClassGenerator;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.BlockLoaderClassGenerator;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.block.*;
import com.github.mouse0w0.peach.mcmod.model.BlockstateTemplate;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.util.ArrayUtils;
import javafx.scene.paint.Color;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GenBlock implements Task {

    @Override
    public void run(Context context) throws Exception {
        BlockLoaderClassGenerator loader = new BlockLoaderClassGenerator(context.getInternalName("block/BlockLoader"), context.getNamespace());
        String classItemGroups = context.getInternalName("init/ItemGroups");

        String classHorizontalBlock = null;
        String classDirectionalBlock = null;
        String classSlabBlock = null;
        String classFenceGateBlock = null;
        String classWallBlock = null;
        String classPaneBlock = null;

        for (BlockElement block : context.getElements(BlockElement.class)) {
            String namespace = context.getNamespace();
            String identifier = block.getIdentifier();

            BlockClassGenerator cg = new BlockClassGenerator(context.getInternalName("block/Block" + JavaUtils.lowerUnderscoreToUpperCamel(identifier)));
            BlockType type = block.getType();
            switch (type) {
                case NORMAL:
                case DIRT:
                case STONE:
                    cg.visitBlock("net/minecraft/block/Block", block.getMaterial(), block.getMapColor());
                    break;
                case PILLAR:
                    cg.visitBlock("net/minecraft/block/BlockRotatedPillar", block.getMaterial(), block.getMapColor());
                    break;
                case HORIZONTAL_OPPOSITE:
                    cg.visitHorizontalOpposite();
                case HORIZONTAL:
                    if (classHorizontalBlock == null) {
                        classHorizontalBlock = context.getInternalName("block/base/BlockHorizontalBase");
                        context.getClassesFiler().write(classHorizontalBlock + ".class", new BlockHorizontalBase(classHorizontalBlock).toByteArray());
                    }
                    cg.visitBlock(classHorizontalBlock, block.getMaterial(), block.getMapColor());
                    break;
                case DIRECTIONAL_OPPOSITE:
                    cg.visitDirectionalOpposite();
                case DIRECTIONAL:
                    if (classDirectionalBlock == null) {
                        classDirectionalBlock = context.getInternalName("block/base/BlockDirectionalBase");
                        context.getClassesFiler().write(classDirectionalBlock + ".class", new BlockDirectionalBase(classDirectionalBlock).toByteArray());
                    }
                    cg.visitBlock(classDirectionalBlock, block.getMaterial(), block.getMapColor());
                    break;
                case STAIRS:
                    cg.visitStairsBlock(block.getMaterial(), block.getMapColor());
                    break;
                case SLAB:
                    if (classSlabBlock == null) {
                        String classSlabType = context.getInternalName("block/base/SlabType");
                        context.getClassesFiler().write(classSlabType + ".class", new SlabType(classSlabType).toByteArray());
                        classSlabBlock = context.getInternalName("block/base/BlockSlabBase");
                        context.getClassesFiler().write(classSlabBlock + ".class", new BlockSlabBase(classSlabBlock, classSlabType).toByteArray());
                    }
                    cg.visitBlock(classSlabBlock, block.getMaterial(), block.getMapColor());
                    break;
                case FENCE:
                    cg.visitBlock("net/minecraft/block/BlockFence", block.getMaterial(), block.getMapColor());
                    break;
                case FENCE_GATE:
                    if (classFenceGateBlock == null) {
                        classFenceGateBlock = context.getInternalName("block/base/BlockFenceGateBase");
                        context.getClassesFiler().write(classFenceGateBlock + ".class", new BlockFenceGateBase(classFenceGateBlock).toByteArray());
                    }
                    cg.visitBlock(classFenceGateBlock, block.getMaterial(), block.getMapColor());
                    break;
                case WALL:
                    if (classWallBlock == null) {
                        classWallBlock = context.getInternalName("block/base/BlockWallBase");
                        context.getClassesFiler().write(classWallBlock + ".class", new BlockWallBase(classWallBlock).toByteArray());
                    }
                    cg.visitBlock(classWallBlock, block.getMaterial(), block.getMapColor());
                    break;
                case TRAPDOOR:
                    cg.visitBlock("net/minecraft/block/BlockTrapDoor", block.getMaterial());
                    cg.visitMapColor(block.getMapColor());
                case PANE:
                    if (classPaneBlock == null) {
                        classPaneBlock = context.getInternalName("block/base/BlockPaneBase");
                        context.getClassesFiler().write(classPaneBlock + ".class", new BlockPaneBase(classPaneBlock).toByteArray());
                    }
                    cg.visitBlock(classPaneBlock, block.getMaterial(), block.getMapColor());
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            cg.visitIdentifier(identifier);
            cg.visitTranslationKey(namespace + "." + identifier);
            if (!"NONE".equals(block.getItemGroup())) {
                cg.visitItemGroup(classItemGroups, block.getItemGroup());
            }
            cg.visitSoundType(block.getSoundType());
            if (block.isUnbreakable()) cg.visitHardness(-1);
            else if (block.getHardness() != 0) cg.visitHardness((float) block.getHardness());
            if (block.getResistance() != 0) cg.visitResistance((float) block.getResistance());
            if (block.getSlipperiness() != 0.6) cg.visitSlipperiness((float) block.getSlipperiness());
            if (block.getBrightness() != 0) cg.visitBrightness(block.getBrightness());
            if (block.isTransparency() ? block.getOpacity() != 0 : block.getOpacity() != 255)
                cg.visitOpacity(block.getOpacity());
            if (!"NONE".equals(block.getHarvestTool()))
                cg.visitHarvestToolAndLevel(block.getHarvestTool(), block.getHarvestLevel());
            if (ArrayUtils.isNotEmpty(block.getInformation()))
                cg.visitInformation(namespace, identifier, block.getInformation().length);

            if (block.isTransparency()) cg.visitTransparency();
            if (!"SOLID".equals(block.getRenderType())) cg.visitRenderType(block.getRenderType());
            if (!"NONE".equals(block.getOffsetType())) cg.visitOffsetType(block.getOffsetType());

            if (block.isNoCollision() || !BlockElement.FULL_BLOCK.equals(block.getBoundingBox())) cg.visitNoFullCube();
            if (block.isNoCollision()) cg.visitNoCollision();
            if (!BlockElement.FULL_BLOCK.equals(block.getBoundingBox())) cg.visitBoundingBox(block.getBoundingBox());

            Color beaconColor = Color.valueOf(block.getBeaconColor());
            if (beaconColor.isOpaque()) cg.visitBeaconColor(beaconColor);
            if (block.isBeaconBase()) cg.visitBeaconBase();
            if (block.isClimbable()) cg.visitClimbable();
            if (block.isReplaceable()) cg.visitReplaceable();
            if (block.isCanConnectRedstone()) cg.visitCanConnectRedstone();
            if (block.getRedstonePower() != 0) cg.visitRedstonePower(block.getRedstonePower());
            if (!"NONE".equals(block.getCanPlantPlant())) cg.visitCanPlantPlant(block.getCanPlantPlant());
            if (block.getEnchantPowerBonus() != 0) cg.visitEnchantPowerBonus((float) block.getEnchantPowerBonus());
            if (block.getFlammability() != 0) cg.visitFlammability(block.getFlammability());
            if (block.getFireSpreadSpeed() != 0) cg.visitFireSpreadSpeed(block.getFireSpreadSpeed());
            if (!"INHERIT".equals(block.getPushReaction())) cg.visitPushReaction(block.getPushReaction());
            if (!"INHERIT".equals(block.getAiPathNodeType())) cg.visitAiPathNodeType(block.getAiPathNodeType());
            context.getClassesFiler().write(cg.getThisName() + ".class", cg.toByteArray());

            loader.visitBlock(identifier, cg.getThisName());

            // Copy textures
            Filer assetsFiler = context.getAssetsFiler();

            Path texturesPath = context.getTexturesFolder();
            for (String texture : block.getTextures().values()) {
                assetsFiler.copy(texturesPath.resolve(texture + ".png"), "textures/" + texture + ".png");
            }

            for (String texture : block.getItemTextures().values()) {
                assetsFiler.copy(texturesPath.resolve(texture + ".png"), "textures/" + texture + ".png");
            }

            // Generate block model
            ModelManager modelManager = context.getModelManager();
            BlockstateTemplate blockstateTemplate = modelManager.getBlockstateTemplate(block.getType().getBlockstate());
            Identifier blockModel = block.getModel();
            Map<String, String> blockTextures = ModelUtils.processTextures(namespace, block.getTextures());
            Map<String, String> outputModels = new HashMap<>();
            String particleTexture = block.getParticleTexture() != null ?
                    ModelUtils.processResourcePath(namespace, block.getParticleTexture()) : null;
            if (ModelManager.CUSTOM.equals(blockModel)) {
                ModelUtils.generateCustomModel(namespace, identifier, blockstateTemplate, block.getCustomModels(), context.getModelsFolder(),
                        blockTextures, particleTexture, assetsFiler.getRoot(), outputModels);
            } else {
                ModelUtils.generateModel(namespace, identifier, blockstateTemplate, modelManager.getModelTemplate(blockModel),
                        blockTextures, particleTexture, assetsFiler.getRoot(), outputModels);
            }

            // Generate block item model
            Identifier itemModel = block.getItemModel();
            if (ModelManager.CUSTOM.equals(itemModel)) {
                ModelUtils.generateCustomModel(namespace, identifier, blockstateTemplate, block.getCustomItemModels(), context.getModelsFolder(),
                        ModelUtils.processTextures(namespace, block.getItemTextures()), null, assetsFiler.getRoot(), outputModels);
            } else if (ModelManager.DEFAULT.equals(itemModel)) {
                ModelUtils.generateModel(namespace, identifier, blockstateTemplate, modelManager.getModelTemplate(blockstateTemplate.getItem()),
                        blockTextures, null, assetsFiler.getRoot(), outputModels);
            } else {
                ModelUtils.generateModel(namespace, identifier, blockstateTemplate, modelManager.getModelTemplate(itemModel),
                        ModelUtils.processTextures(namespace, block.getItemTextures()), null, assetsFiler.getRoot(), outputModels);
            }

            // Generate Blockstate
            BlockstateUtils.generateBlockstate(blockstateTemplate, namespace, outputModels, assetsFiler.resolve("blockstates/" + identifier + ".json"));
        }

        context.getClassesFiler().write(loader.getThisName() + ".class", loader.toByteArray());
    }
}
