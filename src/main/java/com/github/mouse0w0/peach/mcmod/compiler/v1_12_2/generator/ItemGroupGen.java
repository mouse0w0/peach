package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.coffeemaker.CoffeeMaker;
import com.github.mouse0w0.coffeemaker.evaluator.SimpleEvaluator;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroup;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.util.JavaUtils;

import java.util.*;

public class ItemGroupGen extends Generator<ItemGroup> {

    private final List<String> itemGroupClasses = new ArrayList<>();

    private Template templateItemGroup;
    private Template templateModItemGroups;

    private String namespace;
    private String packageName;

    @Override
    protected void before(Environment environment, Collection<ItemGroup> elements) throws Exception {
        CoffeeMaker coffeeMaker = environment.getCoffeeMaker();
        templateItemGroup = coffeeMaker.getTemplate("template/itemGroup/TemplateItemGroup");
        templateModItemGroups = coffeeMaker.getTemplate("template/itemGroup/ModItemGroups");
        namespace = environment.getModSettings().getId();
        packageName = environment.getRootPackageName() + ".itemGroup";
    }

    @Override
    protected void after(Environment environment, Collection<ItemGroup> elements) throws Exception {
        String internalName = ASMUtils.getInternalName(packageName + ".ModItemGroups");
        Map<String, Object> map = Collections.singletonMap("itemGroupClasses", itemGroupClasses);
        byte[] bytes = templateModItemGroups.process(internalName, new SimpleEvaluator(map));
        environment.getClassesFiler().write(internalName + ".class", bytes);
    }

    @Override
    protected void generate(Environment environment, ItemGroup itemGroup) throws Exception {
        String internalName = ASMUtils.getInternalName(packageName, JavaUtils.lowerUnderscoreToUpperCamel(itemGroup.getRegisterName()) + "ItemGroup");

        itemGroupClasses.add(internalName);

        Map<String, Object> map = new HashMap<>();
        map.put("registerName", itemGroup.getRegisterName());
        map.put("translationKey", "itemGroup." + namespace + "." + itemGroup.getRegisterName());
        map.put("icon", itemGroup.getIcon());
        map.put("hasSearchBar", itemGroup.isHasSearchBar());

        byte[] bytes = templateItemGroup.process(internalName, new SimpleEvaluator(map));
        environment.getClassesFiler().write(internalName + ".class", bytes);
    }
}
