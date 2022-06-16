package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.compiler.Filer;
import com.github.mouse0w0.peach.mcmod.compiler.task.Task;
import com.github.mouse0w0.peach.mcmod.compiler.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.compiler.util.ModelUtils;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.ItemClassGenerator;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.ItemGroupsClassGenerator;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.ItemLoaderClassGenerator;
import com.github.mouse0w0.peach.mcmod.element.ElementTypes;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItem;
import com.github.mouse0w0.peach.mcmod.model.ModelEntry;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.model.ModelPrototype;
import com.github.mouse0w0.peach.util.ClassPathUtils;
import com.github.mouse0w0.peach.util.StringUtils;

import java.nio.file.Path;
import java.util.Map;

public class GenItem implements Task {

    @Override
    public void run(Context context) throws Exception {
        ItemLoaderClassGenerator loader = new ItemLoaderClassGenerator(context.getInternalName("item/ItemLoader"), context.getNamespace());
        ItemGroupsClassGenerator groups = new ItemGroupsClassGenerator(context.getInternalName("item/ItemGroups"));

        for (MEItem item : context.getElements(ElementTypes.ITEM)) {
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
                cg.visitArmorItem(namespace + "$" + identifier, item.getEquipSound().getId());

            cg.visitIdentifier(namespace + ":" + identifier);
            cg.visitTranslationKey(namespace + "." + identifier);
            String itemGroup = item.getItemGroup().getId();
            groups.visitItemGroup(itemGroup);
            cg.visitItemGroup(groups.getThisName(), itemGroup);
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
            for (EnchantmentType enchantmentType : item.getAcceptableEnchantments()) {
                cg.visitAcceptableEnchantment(enchantmentType);
            }
            if (!item.getRepairItem().isAir()) cg.visitRepairItem(item.getRepairItem());
            if (!item.getRecipeRemain().isAir()) cg.visitContainerItem(item.getRecipeRemain());
            if (item.getUseAnimation() != UseAnimation.NONE) cg.visitUseAnimation(item.getUseAnimation());
            if (item.getUseDuration() != 0) cg.visitUseDuration(item.getUseDuration());
            if (item.getHitEntityLoss() != 0) cg.visitHitEntityLoss(item.getHitEntityLoss());
            if (item.getDestroyBlockLoss() != 0) cg.visitDestroyBlockLoss(item.getDestroyBlockLoss());
            if (StringUtils.notEmpty(item.getInformation())) cg.visitInformation(item.getInformation());

            if (item.isHasEffect()) cg.visitHasEffect();
            if (type == ItemType.ARMOR && StringUtils.notEmpty(item.getArmorTexture()))
                cg.visitArmorTexture(item.getArmorTexture());

            if (item.getFuelBurnTime() != 0) cg.visitFuelBurnTime(item.getFuelBurnTime());
            if (type == ItemType.FOOD) {
                if (item.isAlwaysEdible()) cg.visitAlwaysEdible();
                if (!item.getFoodContainer().isAir()) cg.visitFoodContainerItem(item.getFoodContainer());
            }
            context.getClassesFiler().write(cg.getThisName() + ".class", cg.toByteArray());

            loader.visitItem(identifier, cg.getThisName());

            // Copy textures
            Filer assetsFiler = context.getAssetsFiler();

            Path texturesPath = context.getProjectStructure().getTextures();
            for (String texture : item.getTextures().values()) {
                assetsFiler.copy(texturesPath.resolve(texture + ".png"), "textures/" + texture + ".png");
            }

            if (item.getType() == ItemType.ARMOR) {
                String armorTexture = item.getArmorTexture();
                assetsFiler.copy(texturesPath.resolve(armorTexture), "textures/" + armorTexture);
            }

            // Generate models
            Map<String, String> textures = ModelUtils.preprocessTextures(namespace, item.getTextures(), null);

            Identifier model = item.getModelPrototype();
            if (ModelManager.CUSTOM.equals(model)) {
                Path projectModelsPath = context.getProjectStructure().getModels();
                String itemModel = item.getModels().get("item");
                if (StringUtils.isNotEmpty(itemModel)) {
                    Path source = projectModelsPath.resolve(itemModel);
                    Path target = assetsFiler.resolve("models/item/" + identifier + ".json");
                    ModelUtils.applyModel(source, target, textures, false);
                }
            } else {
                ModelManager manager = ModelManager.getInstance();
                ModelPrototype prototype = manager.getModelPrototype(model);
                for (Map.Entry<String, ModelEntry> entry : prototype.getModels().entrySet()) {
                    ModelEntry modelEntry = entry.getValue();
                    String modelName = modelEntry.getName().replace("${identifier}", identifier);
                    Path source = ClassPathUtils.getPath("template/" + modelEntry.getTemplate());
                    Path target = assetsFiler.resolve("models/" + modelName + ".json");
                    ModelUtils.applyModel(source, target, textures, false);
                }
            }
        }

        context.getClassesFiler().write(loader.getThisName() + ".class", loader.toByteArray());
        context.getClassesFiler().write(groups.getThisName() + ".class", groups.toByteArray());
    }
}
