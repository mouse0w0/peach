package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.SmeltingRecipeLoaderClassGenerator;
import com.github.mouse0w0.peach.mcmod.element.impl.MESmeltingRecipe;

import java.util.Collection;

public class SmeltingRecipeGenerator extends Generator<MESmeltingRecipe> {

    private SmeltingRecipeLoaderClassGenerator cg;

    @Override
    protected void before(Context context, Collection<MESmeltingRecipe> elements) throws Exception {
        cg = new SmeltingRecipeLoaderClassGenerator(context.getInternalName("SmeltingRecipeLoader"));
    }

    @Override
    protected void generate(Context context, MESmeltingRecipe smelting) throws Exception {
        cg.visitSmelting(smelting);
    }

    @Override
    protected void after(Context context, Collection<MESmeltingRecipe> elements) throws Exception {
        context.getClassesFiler().write(cg.getThisName() + ".class", cg.toByteArray());
    }
}
