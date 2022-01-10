package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator.*;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.Map;

public class ElementTask implements CompileTask {

    private final Map<String, Generator<?>> generatorMap = new HashMap<>();

    public ElementTask() {
        generatorMap.put("item", new ItemGenerator());
        generatorMap.put("item_group", new ItemGroupGenerator());
        generatorMap.put("crafting", new CraftingRecipeGenerator());
        generatorMap.put("smelting", new SmeltingRecipeGenerator());
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void run(Context context) throws Exception {
        Multimap<ElementType<?>, Element> elements = context.getElements();

        for (ElementType<?> type : ElementRegistry.getInstance().getElementTypes()) {
            Generator generator = generatorMap.get(type.getName());
            if (generator == null) continue;
            generator.generate(context, elements.get(type));
        }
    }
}
