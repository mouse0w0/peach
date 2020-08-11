package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element.CraftingRecipeGen;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element.ElementGen;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element.ItemGen;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element.SmeltingRecipeGen;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementDefinition;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.Map;

public class ElementTask implements CompileTask {

    @SuppressWarnings("rawtypes")
    private final Map<String, ElementGen> elementGenMap = new HashMap<>();

    public ElementTask() {
        elementGenMap.put("item", new ItemGen());
        elementGenMap.put("crafting_recipe", new CraftingRecipeGen());
        elementGenMap.put("smelting_recipe", new SmeltingRecipeGen());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(Environment environment) throws Exception {
        Multimap<ElementDefinition<?>, Element<?>> elements = environment.getElements();

        for (ElementDefinition<?> definition : ElementManager.getInstance().getElements()) {
            elementGenMap.get(definition.getId()).generate(environment, elements.get(definition));
        }
    }
}
