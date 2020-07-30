package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileContext;
import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.CompilerImpl;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element.CraftingRecipeGen;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element.ElementGen;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element.SmeltingRecipeGen;
import com.github.mouse0w0.peach.mcmod.element.ElementDefinition;
import com.github.mouse0w0.peach.mcmod.element.ElementFile;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ElementTask implements CompileTask {

    @SuppressWarnings("rawtypes")
    private final Map<String, ElementGen> elementGenMap = new HashMap<>();

    public ElementTask() {
        elementGenMap.put("crafting_recipe", new CraftingRecipeGen());
        elementGenMap.put("smelting_recipe", new SmeltingRecipeGen());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(CompileContext context) throws Exception {
        Path sources = context.getData(CompilerImpl.PROJECT_SOURCES_PATH);
        ElementManager elementManager = ElementManager.getInstance();

        Multimap<ElementDefinition<?>, ElementFile<?>> loadedElementFiles = HashMultimap.create();

        Iterator<Path> iterator = Files.walk(sources)
                .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json"))
                .iterator();
        while (iterator.hasNext()) {
            Path file = iterator.next();
            ElementDefinition<?> element = elementManager.getElement(file);
            ElementFile<?> elementFile = element.load(file);
            elementFile.load();
            loadedElementFiles.put(element, elementFile);
        }

        for (ElementDefinition<?> definition : ElementManager.getInstance().getElements()) {
            elementGenMap.get(definition.getId()).generate(context, loadedElementFiles.get(definition));
        }
    }
}
