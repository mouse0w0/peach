package com.github.mouse0w0.peach.mcmod.generator.v1_12_2;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.impl.BlockElement;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.mcmod.generator.Filer;
import com.github.mouse0w0.peach.mcmod.generator.task.Task;
import com.github.mouse0w0.peach.mcmod.generator.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.generator.util.ModelUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ItemClassGenerator;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ItemLoaderClassGenerator;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.item.ItemBlockBase;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.item.ItemSlabBase;
import com.github.mouse0w0.peach.mcmod.model.BlockstateTemplate;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.util.ArrayUtils;
import com.github.mouse0w0.peach.util.StringUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GenItem implements Task {

    @Override
    public void run(Context context) throws Exception {
        ItemLoaderClassGenerator loader = new ItemLoaderClassGenerator(context.getInternalName("item/ItemLoader"), context.getNamespace());

        String classBlockLoader = context.getInternalName("block/BlockLoader");
        String classBlockItem = null;
        String classSlabItem = null;
        for (BlockElement block : context.getElements(BlockElement.class)) {
            if (block.isDoNotRegisterItem()) continue;
            if (Objects.requireNonNull(block.getType()) == BlockType.SLAB) {
                if (classSlabItem == null) {
                    classSlabItem = context.getInternalName("item/base/ItemSlabBase");
                    context.getClassesFiler().write(classSlabItem + ".class",
                            new ItemSlabBase(
                                    classSlabItem,
                                    context.getInternalName("block/base/BlockSlabBase"),
                                    context.getInternalName("block/base/SlabType"))
                                    .toByteArray());
                }
                loader.visitBlockItem(block.getIdentifier(), classSlabItem, classBlockLoader);
            } else {
                if (classBlockItem == null) {
                    classBlockItem = context.getInternalName("item/base/ItemBlockBase");
                    context.getClassesFiler().write(classBlockItem + ".class",
                            new ItemBlockBase(classBlockItem).toByteArray());
                }
                loader.visitBlockItem(block.getIdentifier(), classBlockItem, classBlockLoader);
            }
        }

        String classItemGroups = context.getInternalName("init/ItemGroups");
        for (ItemElement item : context.getElements(ItemElement.class)) {
            String namespace = context.getNamespace();
            String identifier = item.getIdentifier();

            // Generate class
            ItemClassGenerator cg = new ItemClassGenerator(context.getInternalName("item/Item" + JavaUtils.lowerUnderscoreToUpperCamel(identifier)));
            ItemType type = item.getType();
            if (type == ItemType.NORMAL)
                cg.visitNormalItem();
            else if (type == ItemType.FOOD)
                cg.visitFoodItem(item.getHunger(), (float) item.getSaturation(), item.isWolfFood());
            else if (type == ItemType.TOOL)
                cg.visitToolItem();
            else if (type == ItemType.SWORD)
                cg.visitSwordItem(namespace + "$" + identifier, 0);
            else if (type == ItemType.ARMOR)
                cg.visitArmorItem(namespace + "$" + identifier, item.getEquipSound());

            cg.visitIdentifier(identifier);
            cg.visitTranslationKey(namespace + "." + identifier);
            if (!"NONE".equals(item.getItemGroup())) {
                cg.visitItemGroup(classItemGroups, item.getItemGroup());
            }
            cg.visitMaxStackSize(item.getMaxStackSize());
            if (item.getDurability() != 0) cg.visitDurability(item.getDurability());
            if (item.getDestroySpeed() != 1D) cg.visitDestroySpeed((float) item.getDestroySpeed());
            if (item.isCanDestroyAnyBlock()) cg.visitCanDestroyAnyBlock();
            if (item.getAttackDamage() != 1D)
                cg.visitAttackDamage(item.getAttackDamage() - 1D, item.getType() == ItemType.SWORD);
            if (item.getAttackSpeed() != 4D) cg.visitAttackSpeed(item.getAttackSpeed() - 4D);
            if (item.getEquipmentSlot() != EquipmentSlot.NONE) {
                cg.visitEquipmentSlot(item.getEquipmentSlot());
                for (AttributeModifier attributeModifier : item.getAttributeModifiers()) {
                    cg.visitAttributeModifier(attributeModifier);
                }
            }
            for (ToolAttribute toolAttribute : item.getToolAttributes()) {
                cg.visitToolAttribute(toolAttribute);
            }
            if (item.getEnchantability() != 0) cg.visitEnchantability(item.getEnchantability());
            for (String enchantmentType : item.getAcceptableEnchantments()) {
                cg.visitAcceptableEnchantment(enchantmentType);
            }
            if (!item.getRepairItem().isAir()) cg.visitRepairItem(context, item.getRepairItem());
            if (!item.getRecipeRemain().isAir()) cg.visitContainerItem(context, item.getRecipeRemain());
            if (!"NONE".equals(item.getUseAnimation())) cg.visitUseAnimation(item.getUseAnimation());
            if (item.getUseDuration() != 0) cg.visitUseDuration(item.getUseDuration());
            if (item.getHitEntityLoss() != 0) cg.visitHitEntityLoss(item.getHitEntityLoss());
            if (item.getDestroyBlockLoss() != 0) cg.visitDestroyBlockLoss(item.getDestroyBlockLoss());
            if (ArrayUtils.isNotEmpty(item.getInformation()))
                cg.visitInformation(namespace, identifier, item.getInformation().length);

            if (item.isHasEffect()) cg.visitHasEffect();
            if (type == ItemType.ARMOR && StringUtils.isNotEmpty(item.getArmorTexture()))
                cg.visitArmorTexture(item.getArmorTexture());

            if (item.getFuelBurnTime() != 0) cg.visitFuelBurnTime(item.getFuelBurnTime());
            if (type == ItemType.FOOD) {
                if (item.isAlwaysEdible()) cg.visitAlwaysEdible();
                if (!item.getFoodContainer().isAir()) cg.visitFoodContainerItem(context, item.getFoodContainer());
            }
            context.getClassesFiler().write(cg.getThisName() + ".class", cg.toByteArray());

            loader.visitItem(identifier, cg.getThisName());

            // Copy textures
            Filer assetsFiler = context.getAssetsFiler();

            Path texturesPath = context.getTexturesFolder();
            for (String texture : item.getTextures().values()) {
                assetsFiler.copy(texturesPath.resolve(texture + ".png"), "textures/" + texture + ".png");
            }

            if (item.getType() == ItemType.ARMOR) {
                String armorTexture = item.getArmorTexture();
                assetsFiler.copy(texturesPath.resolve(armorTexture), "textures/" + armorTexture);
            }

            // Generate models
            ModelManager modelManager = context.getModelManager();
            BlockstateTemplate blockstateTemplate = modelManager.getBlockstateTemplate("item");
            Identifier model = item.getModel();
            Map<String, String> textures = ModelUtils.processTextures(namespace, item.getTextures());
            if (ModelManager.CUSTOM.equals(model)) {
                ModelUtils.generateCustomModel(namespace, identifier, blockstateTemplate, item.getCustomModels(), context.getModelsFolder(),
                        textures, null, assetsFiler.getRoot(), new HashMap<>());
            } else {
                ModelUtils.generateModel(namespace, identifier, blockstateTemplate, modelManager.getModelTemplate(model),
                        textures, null, assetsFiler.getRoot(), new HashMap<>());
            }
        }

        context.getClassesFiler().write(loader.getThisName() + ".class", loader.toByteArray());
    }
}
