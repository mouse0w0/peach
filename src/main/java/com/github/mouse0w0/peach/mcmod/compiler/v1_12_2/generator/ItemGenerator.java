package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.compiler.Filer;
import com.github.mouse0w0.peach.mcmod.compiler.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.ItemClassGenerator;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.ItemGroupsClassGenerator;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.ItemLoaderClassGenerator;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItem;
import com.github.mouse0w0.peach.util.StringUtils;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemGenerator extends Generator<MEItem> {

    private ItemLoaderClassGenerator itemLoaderClassGenerator;
    private ItemGroupsClassGenerator itemGroupsClassGenerator;

    @Override
    protected void before(Context context, Collection<MEItem> elements) throws Exception {
        itemLoaderClassGenerator = new ItemLoaderClassGenerator(context.getInternalName("item/ItemLoader"), context.getNamespace());
        itemGroupsClassGenerator = new ItemGroupsClassGenerator(context.getInternalName("item/ItemGroups"));
    }

    @Override
    protected void after(Context context, Collection<MEItem> elements) throws Exception {
        context.getClassesFiler().write(itemLoaderClassGenerator.getThisName() + ".class", itemLoaderClassGenerator.toByteArray());
        context.getClassesFiler().write(itemGroupsClassGenerator.getThisName() + ".class", itemGroupsClassGenerator.toByteArray());
    }

    @Override
    protected void generate(Context context, MEItem item) throws Exception {
        String namespace = context.getNamespace();
        String identifier = item.getIdentifier();

        ItemClassGenerator cg = new ItemClassGenerator(context.getInternalName("item/" + JavaUtils.lowerUnderscoreToUpperCamel(identifier)) + "Item");
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
        itemGroupsClassGenerator.visitItemGroup(itemGroup);
        cg.visitItemGroup(itemGroupsClassGenerator.getThisName(), itemGroup);
        cg.visitMaxStackSize(item.getMaxStackSize());
        if (item.getDurability() != 0) cg.visitDurability(item.getDurability());
        if (item.getDestroySpeed() != 1) cg.visitDestroySpeed((float) item.getDestroySpeed());
        if (item.isCanDestroyAnyBlock()) cg.visitCanDestroyAnyBlock();
        for (ToolAttribute toolAttribute : item.getToolAttributes())
            cg.visitToolAttribute(toolAttribute);
        for (AttributeModifier attributeModifier : item.getAttributeModifiers())
            cg.visitAttributeModifier(attributeModifier);
        if (item.getEnchantability() != 0) cg.visitEnchantability(item.getEnchantability());
        for (EnchantmentType enchantmentType : item.getAcceptableEnchantments())
            cg.visitAcceptableEnchantment(enchantmentType);
        if (!item.getRepairItem().isAir()) cg.visitRepairItem(item.getRepairItem());
        if (!item.getRecipeRemain().isAir()) cg.visitContainerItem(item.getRecipeRemain());
        if (item.getEquipmentSlot() != EquipmentSlot.MAINHAND) cg.visitEquipmentSlot(item.getEquipmentSlot());
        if (item.getUseAnimation() != UseAnimation.NONE) cg.visitUseAnimation(item.getUseAnimation());
        if (item.getUseDuration() != 0) cg.visitUseDuration(item.getUseDuration());
        if (item.getHitEntityLoss() != 0) cg.visitHitEntityLoss(item.getHitEntityLoss());
        if (item.getDestroyBlockLoss() != 0) cg.visitDestroyBlockLoss(item.getDestroyBlockLoss());
        if (StringUtils.notEmpty(item.getInformation())) cg.visitInformation(item.getInformation());

        if (type == ItemType.ARMOR && StringUtils.notEmpty(item.getArmorTexture()))
            cg.visitArmorTexture(item.getArmorTexture());
        if (item.getFuelBurnTime() != 0) cg.visitFuelBurnTime(item.getFuelBurnTime());
        if (type == ItemType.FOOD && !item.getFoodContainer().isAir())
            cg.visitFoodContainerItem(item.getFoodContainer());
        context.getClassesFiler().write(cg.getThisName() + ".class", cg.toByteArray());

        itemLoaderClassGenerator.visitItem(identifier, cg.getThisName());

//        McModel model = context.getModelManager().getItemModel(item.getModel());
        Map<String, String> textures = new LinkedHashMap<>();
        item.getTextures().forEach((key, value) -> textures.put(key, namespace + ":" + value));
//        model.setTextures(textures);

        Filer assetsFiler = context.getAssetsFiler();
//        try (BufferedWriter writer = assetsFiler.newWriter("models", "item", identifier + ".json")) {
//            McModelHelper.GSON.toJson(model, writer);
//        }

        Path texturesPath = context.getProjectStructure().getTextures();
        for (String texture : item.getTextures().values()) {
            assetsFiler.copy(texturesPath.resolve(texture + ".png"), "textures/" + texture + ".png");
        }

        if (item.getType() == ItemType.ARMOR) {
            String armorTexture = item.getArmorTexture();
            assetsFiler.copy(texturesPath.resolve(armorTexture), "textures/" + armorTexture);
        }
    }
}
