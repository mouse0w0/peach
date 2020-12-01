package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.coffeemaker.CoffeeMaker;
import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.LocalVar;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.peach.mcmod.compiler.Compiler;
import com.github.mouse0w0.peach.mcmod.compiler.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.compiler.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroup;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemGroupGen extends Generator<ItemGroup> {

    private final List<String> classes = new ArrayList<>();

    private Template templateItemGroup;
    private Template templateModItemGroups;

    private String packageName;

    @Override
    protected void before(Compiler compiler, Collection<ItemGroup> elements) throws Exception {
        CoffeeMaker coffeeMaker = compiler.getCoffeeMaker();
        templateItemGroup = coffeeMaker.getTemplate("template/itemGroup/TemplateItemGroup");
        templateModItemGroups = coffeeMaker.getTemplate("template/itemGroup/ModItemGroups");
        packageName = compiler.getRootPackageName() + ".itemGroup";
    }

    @Override
    protected void after(Compiler compiler, Collection<ItemGroup> elements) throws Exception {
        String internalName = ASMUtils.getInternalName(packageName + ".ModItemGroups");
        Evaluator evaluator = compiler.getEvaluator();
        try (LocalVar localVar = evaluator.pushLocalVar()) {
            localVar.put("classes", classes);
            compiler.getClassesFiler().write(internalName + ".class",
                    templateModItemGroups.process(internalName, evaluator));
        }
    }

    @Override
    protected void generate(Compiler compiler, ItemGroup itemGroup) throws Exception {
        String internalName = ASMUtils.getInternalName(packageName, JavaUtils.lowerUnderscoreToUpperCamel(itemGroup.getRegisterName()) + "ItemGroup");
        classes.add(internalName);
        Evaluator evaluator = compiler.getEvaluator();
        try (LocalVar localVar = evaluator.pushLocalVar()) {
            localVar.put("itemGroup", itemGroup);
            compiler.getClassesFiler().write(internalName + ".class",
                    templateItemGroup.process(internalName, evaluator));
        }

        String background = itemGroup.getBackground();
        if (!Strings.isNullOrEmpty(background)) {
            compiler.getAssetsFiler().copy(compiler.getProjectStructure().getTextures().resolve(background), "textures/" + background);
        }
    }
}
