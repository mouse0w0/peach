package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.coffeemaker.evaluator.SimpleEvaluator;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;

import java.util.Collection;
import java.util.Collections;

public class SmeltingRecipeGen extends Generator<SmeltingRecipe> {

    @Override
    public void generate(Environment environment, Collection<SmeltingRecipe> elements) throws Exception {
        String internalName = ASMUtils.getInternalName(environment.getRootPackageName(), "SmeltingRecipes");
        Template template = environment.getCoffeeMaker().getTemplate("template/SmeltingRecipes");
        byte[] bytes = template.process(internalName, new SimpleEvaluator(Collections.singletonMap("recipes", elements)));
        environment.getClassesFiler().write(internalName + ".class", bytes);
    }

    @Override
    protected void generate(Environment environment, SmeltingRecipe smelting) throws Exception {
        // Nothing to do.
    }
}
