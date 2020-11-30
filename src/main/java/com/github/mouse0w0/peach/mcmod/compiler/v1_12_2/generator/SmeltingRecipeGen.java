package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.LocalVar;
import com.github.mouse0w0.coffeemaker.template.Template;
import com.github.mouse0w0.peach.mcmod.compiler.Compiler;
import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;

import java.util.Collection;

public class SmeltingRecipeGen extends Generator<SmeltingRecipe> {

    @Override
    public void generate(Compiler compiler, Collection<SmeltingRecipe> elements) throws Exception {
        String internalName = ASMUtils.getInternalName(compiler.getRootPackageName(), "SmeltingRecipes");
        Template template = compiler.getCoffeeMaker().getTemplate("template/SmeltingRecipes");
        Evaluator evaluator = compiler.getEvaluator();
        try (LocalVar localVar = evaluator.pushLocalVar()) {
            localVar.put("recipes", elements);
            compiler.getClassesFiler().write(internalName + ".class",
                    template.process(internalName, evaluator));
        }
    }

    @Override
    protected void generate(Compiler compiler, SmeltingRecipe smelting) throws Exception {
        // Nothing to do.
    }
}
