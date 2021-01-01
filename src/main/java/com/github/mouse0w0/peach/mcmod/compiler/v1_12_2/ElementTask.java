package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.Compiler;
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
        generatorMap.put("item", new ItemGen());
        generatorMap.put("item_group", new ItemGroupGen());
        generatorMap.put("crafting", new CraftingRecipeGen());
        generatorMap.put("smelting", new SmeltingRecipeGen());
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void run(Compiler compiler) throws Exception {
        Multimap<ElementType<?>, Element> elements = compiler.getElements();

        for (ElementType<?> type : ElementRegistry.getInstance().getElementTypes()) {
            Generator generator = generatorMap.get(type.getName());
            generator.generate(compiler, elements.get(type));
        }
    }
}
