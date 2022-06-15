package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.compiler.task.Task;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.SmeltingRecipeLoaderClassGenerator;
import com.github.mouse0w0.peach.mcmod.element.ElementTypes;
import com.github.mouse0w0.peach.mcmod.element.impl.MESmeltingRecipe;

public class GenSmeltingRecipe implements Task {
    @Override
    public void run(Context context) throws Exception {
        SmeltingRecipeLoaderClassGenerator loader = new SmeltingRecipeLoaderClassGenerator(context.getInternalName("SmeltingRecipeLoader"));
        for (MESmeltingRecipe smelting : context.getElements(ElementTypes.SMELTING_RECIPE)) {
            loader.visitSmelting(smelting);
        }
        context.getClassesFiler().write(loader.getThisName() + ".class", loader.toByteArray());
    }
}
