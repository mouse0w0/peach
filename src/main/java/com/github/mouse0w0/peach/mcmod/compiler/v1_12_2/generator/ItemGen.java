package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.coffeemaker.CoffeeMaker;
import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.LocalVar;
import com.github.mouse0w0.coffeemaker.template.Field;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.peach.mcmod.ItemType;
import com.github.mouse0w0.peach.mcmod.compiler.Compiler;
import com.github.mouse0w0.peach.mcmod.compiler.Filer;
import com.github.mouse0w0.peach.mcmod.compiler.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.compiler.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.model.ItemGroupDef;
import com.github.mouse0w0.peach.mcmod.element.impl.Item;
import com.github.mouse0w0.peach.mcmod.model.mcj.McjModel;
import com.github.mouse0w0.peach.mcmod.model.mcj.McjModelHelper;

import java.io.BufferedWriter;
import java.nio.file.Path;
import java.util.*;

public class ItemGen extends Generator<Item> {

    private static final String ITEM_GROUP_DESCRIPTOR = "Lnet/minecraft/creativetab/CreativeTabs;";

    private final List<Field> items = new ArrayList<>();
    private final List<ItemGroupDef> itemGroups = new ArrayList<>();

    private final Map<ItemType, Template> templates = new EnumMap<>(ItemType.class);

    private String modItemsInternalName;
    private String modItemModelsInternalName;
    private String itemGroupsInternalName;

    private String itemPackageName;
    private String namespace;

    @Override
    protected void before(Compiler compiler, Collection<Item> elements) throws Exception {
        CoffeeMaker coffeeMaker = compiler.getCoffeeMaker();
        templates.put(ItemType.NORMAL, coffeeMaker.getTemplate("template/item/TemplateItem"));
        templates.put(ItemType.SWORD, coffeeMaker.getTemplate("template/item/TemplateSword"));
        templates.put(ItemType.TOOL, coffeeMaker.getTemplate("template/item/TemplateTool"));
        templates.put(ItemType.ARMOR, coffeeMaker.getTemplate("template/item/TemplateArmor"));
        templates.put(ItemType.FOOD, coffeeMaker.getTemplate("template/item/TemplateFood"));

        String packageName = compiler.getRootPackageName();
        namespace = compiler.getMetadata().getId();

        itemPackageName = packageName + ".item";
        modItemsInternalName = ASMUtils.getInternalName(packageName + ".item.ModItems");
        modItemModelsInternalName = ASMUtils.getInternalName(packageName + ".client.item.ModItemModels");
        itemGroupsInternalName = ASMUtils.getInternalName(packageName + ".init.ItemGroups");
    }

    @Override
    protected void after(Compiler compiler, Collection<Item> elements) throws Exception {
        CoffeeMaker coffeeMaker = compiler.getCoffeeMaker();
        Filer classesFiler = compiler.getClassesFiler();

        Map<String, Object> map = new HashMap<>();
        map.put("modid", compiler.getMetadata().getId());
        map.put("items", items);
        Evaluator evaluator = compiler.getEvaluator();
        try (LocalVar localVar = evaluator.pushLocalVar()) {
            localVar.put("items", items);
            localVar.put("itemGroups", itemGroups);

            classesFiler.write(modItemsInternalName + ".class",
                    coffeeMaker.getTemplate("template/item/ModItems")
                            .process(modItemsInternalName, evaluator));
            classesFiler.write(modItemModelsInternalName + ".class",
                    coffeeMaker.getTemplate("template/client/item/ModItemModels")
                            .process(modItemModelsInternalName, evaluator));
            classesFiler.write(itemGroupsInternalName + ".class",
                    coffeeMaker.getTemplate("template/init/ItemGroups")
                            .process(itemGroupsInternalName, evaluator));
        }
    }

    @Override
    protected void generate(Compiler compiler, Item item) throws Exception {
        String identifier = item.getIdentifier();
        String internalName = ASMUtils.getInternalName(itemPackageName, JavaUtils.lowerUnderscoreToUpperCamel(identifier));
        items.add(new Field(modItemsInternalName, JavaUtils.lowerUnderscoreToUpperUnderscore(identifier), ASMUtils.getDescriptor(internalName)));

        String itemGroup = item.getItemGroup();
        ItemGroupDef itemGroupDef = new ItemGroupDef(itemGroup,
                new Field(itemGroupsInternalName, itemGroup.toUpperCase(), ITEM_GROUP_DESCRIPTOR));
        itemGroups.add(itemGroupDef);

        Evaluator evaluator = compiler.getEvaluator();
        try (LocalVar localVar = evaluator.pushLocalVar()) {
            localVar.put("item", item);
            localVar.put("ITEM_GROUPS_CLASS", itemGroupsInternalName);

            compiler.getClassesFiler().write(internalName + ".class",
                    templates.get(item.getItemType()).process(internalName, evaluator));
        }

        McjModel model = compiler.getModelManager().getItemModel(item.getModel());
        Map<String, String> textures = new LinkedHashMap<>();
        item.getTextures().forEach((key, value) -> textures.put(key, namespace + ":" + value));
        model.setTextures(textures);

        Filer assetsFiler = compiler.getAssetsFiler();
        try (BufferedWriter writer = assetsFiler.newWriter("models", "item", identifier + ".json")) {
            McjModelHelper.GSON.toJson(model, writer);
        }

        Path texturesPath = compiler.getProjectStructure().getTextures();
        for (String texture : item.getTextures().values()) {
            assetsFiler.copy(texturesPath.resolve(texture + ".png"), "textures/" + texture + ".png");
        }

        if (item.getItemType() == ItemType.ARMOR) {
            String armorTexture = item.getArmorTexture();
            assetsFiler.copy(texturesPath.resolve(armorTexture), "textures/" + armorTexture);
        }
    }
}
