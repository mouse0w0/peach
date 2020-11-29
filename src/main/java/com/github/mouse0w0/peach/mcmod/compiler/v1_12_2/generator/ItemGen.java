package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.coffeemaker.CoffeeMaker;
import com.github.mouse0w0.coffeemaker.evaluator.SimpleEvaluator;
import com.github.mouse0w0.coffeemaker.template.Field;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.compiler.Filer;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.model.ItemGroupDef;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.model.json.JsonModel;
import com.github.mouse0w0.peach.mcmod.model.json.JsonModelHelper;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.util.JavaUtils;

import java.io.BufferedWriter;
import java.nio.file.Path;
import java.util.*;

public class ItemGen extends Generator<ItemElement> {

    private static final String ITEM_GROUP_DESCRIPTOR = "Lnet/minecraft/creativetab/CreativeTabs;";

    private final List<Field> items = new ArrayList<>();
    private final List<ItemGroupDef> itemGroups = new ArrayList<>();

    private Template templateItem;

    private String modItemsInternalName;
    private String modItemModelsInternalName;
    private String itemGroupsInternalName;

    private String itemPackageName;
    private String namespace;

    public static String getItemFieldName(String registerName) {
        return registerName.toUpperCase();
    }

    public static String getItemClassName(String registerName) {
        return JavaUtils.lowerUnderscoreToUpperCamel(registerName);
    }

    @Override
    protected void before(Environment environment, Collection<ItemElement> elements) throws Exception {
        templateItem = environment.getCoffeeMaker().getTemplate("template/item/TemplateItem");

        String packageName = environment.getRootPackageName();
        namespace = environment.getMetadata().getId();

        itemPackageName = packageName + ".item";
        modItemsInternalName = ASMUtils.getInternalName(packageName + ".item.ModItems");
        modItemModelsInternalName = ASMUtils.getInternalName(packageName + ".client.item.ModItemModels");
        itemGroupsInternalName = ASMUtils.getInternalName(packageName + ".init.ItemGroups");
    }

    @Override
    protected void after(Environment environment, Collection<ItemElement> elements) throws Exception {
        CoffeeMaker coffeeMaker = environment.getCoffeeMaker();
        Filer classesFiler = environment.getClassesFiler();

        Map<String, Object> map = new HashMap<>();
        map.put("modid", environment.getMetadata().getId());
        map.put("items", items);
        SimpleEvaluator itemsEvaluator = new SimpleEvaluator(map);
        classesFiler.write(modItemsInternalName + ".class",
                coffeeMaker.getTemplate("template/item/ModItems")
                        .process(modItemsInternalName, itemsEvaluator));
        classesFiler.write(modItemModelsInternalName + ".class",
                coffeeMaker.getTemplate("template/client/item/ModItemModels")
                        .process(modItemModelsInternalName, itemsEvaluator));

        classesFiler.write(itemGroupsInternalName + ".class",
                coffeeMaker.getTemplate("template/init/ItemGroups")
                        .process(itemGroupsInternalName, new SimpleEvaluator(itemGroups)));
    }

    @Override
    protected void generate(Environment environment, ItemElement item) throws Exception {
        String registerName = item.getRegisterName();
        String internalName = ASMUtils.getInternalName(itemPackageName, ItemGen.getItemClassName(registerName));
        items.add(new Field(modItemsInternalName, ItemGen.getItemFieldName(registerName), ASMUtils.getDescriptor(internalName)));

        String itemGroup = item.getItemGroup();
        ItemGroupDef itemGroupDef = new ItemGroupDef(itemGroup,
                new Field(itemGroupsInternalName, itemGroup.toUpperCase(), ITEM_GROUP_DESCRIPTOR));
        itemGroups.add(itemGroupDef);

        Map<String, Object> map = new HashMap<>();
        map.put("registerName", namespace + ":" + item.getRegisterName());
        map.put("translationKey", namespace + "." + item.getRegisterName());
        map.put("itemGroup", itemGroupDef.field);
        map.put("maxStackSize", item.getMaxStackSize());
        map.put("hasEffect", item.isHasEffect());

        byte[] bytes = templateItem.process(internalName, new SimpleEvaluator(map));
        environment.getClassesFiler().write(internalName + ".class", bytes);

        JsonModel model = environment.getModelManager().getItemModel(item.getModel());
        Map<String, String> textures = new LinkedHashMap<>();
        item.getTextures().forEach((key, value) -> textures.put(key, namespace + ":" + value));
        model.setTextures(textures);

        Filer assetsFiler = environment.getAssetsFiler();
        try (BufferedWriter writer = assetsFiler.newWriter("models", "item", registerName + ".json")) {
            JsonModelHelper.GSON.toJson(model, writer);
        }

        for (String texture : item.getTextures().values()) {
            Path textureFile = getItemTextureFilePath(environment, texture);
            assetsFiler.copy(textureFile, "textures/" + texture + ".png");
        }
    }

    private Path getItemTextureFilePath(Environment environment, String textureName) {
        return environment.getSourceDirectory().resolve("resources/textures/" + textureName + ".png");
    }
}
