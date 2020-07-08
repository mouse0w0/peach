package com.github.mouse0w0.peach.forge.compiler.v1_12_2;

import com.github.mouse0w0.peach.forge.compiler.CompileContext;
import com.github.mouse0w0.peach.forge.compiler.CompileTask;
import com.github.mouse0w0.peach.forge.compiler.ForgeCompiler;
import com.github.mouse0w0.peach.forge.compiler.v1_12_2.element.CraftingRecipeGen;
import com.github.mouse0w0.peach.forge.compiler.v1_12_2.element.ElementGen;
import com.github.mouse0w0.peach.forge.element.ElementDefinition;
import com.github.mouse0w0.peach.forge.element.ElementFile;
import com.github.mouse0w0.peach.forge.element.ElementManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ElementTask implements CompileTask {

    private final Map<String, ElementGen> elementGenMap = new HashMap<>();

    public ElementTask() {
        elementGenMap.put("crafting_recipe", new CraftingRecipeGen());
    }

    @Override
    public void run(CompileContext context) throws Exception {
        Path sources = context.getData(ForgeCompiler.PROJECT_SOURCES_PATH);
        ElementManager elementManager = ElementManager.getInstance();

        Iterator<Path> iterator = Files.walk(sources)
                .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json"))
                .iterator();
        while (iterator.hasNext()) {
            Path file = iterator.next();
            String fileName = file.getFileName().toString();
            String elementId = fileName.substring(fileName.indexOf('.') + 1, fileName.lastIndexOf('.'));
            ElementDefinition<?> element = elementManager.getElement(elementId);
            ElementFile<?> elementFile = element.load(file);
            elementFile.load();
            elementGenMap.get(elementId).generate(context, elementFile);
        }
    }
}
