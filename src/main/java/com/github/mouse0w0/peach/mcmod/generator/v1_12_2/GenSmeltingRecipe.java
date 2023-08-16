package com.github.mouse0w0.peach.mcmod.generator.v1_12_2;

import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingElement;
import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.mcmod.generator.task.Task;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.SmeltingRecipeLoaderClassGenerator;

public class GenSmeltingRecipe implements Task {
    @Override
    public void run(Context context) throws Exception {
        SmeltingRecipeLoaderClassGenerator loader = new SmeltingRecipeLoaderClassGenerator(context.getInternalName("SmeltingRecipeLoader"));
        for (SmeltingElement smelting : context.getElements(SmeltingElement.class)) {
            loader.visitSmelting(smelting);
        }
        context.getClassesFiler().write(loader.getThisName() + ".class", loader.toByteArray());
    }
}
