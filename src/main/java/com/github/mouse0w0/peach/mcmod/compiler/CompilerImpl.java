package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.coffeemaker.CoffeeMaker;
import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.nashorn.NashornEvaluator;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.*;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CompilerImpl implements Compiler {
    private final ProjectStructure projectStructure;
    private final Path outputFolder;

    private final Messager messager = new MessagerImpl();

    private McModMetadata metadata;

    private final ElementRegistry elementRegistry = ElementRegistry.getInstance();

    private String rootPackageName;

    private Multimap<ElementType<?>, Element> elements;

    private ModelManager modelManager;

    private CoffeeMaker coffeeMaker;
    private Evaluator evaluator;

    private Filer classesFiler;
    private Filer resourcesFiler;
    private Filer assetsFiler;

    private final List<CompileTask> taskList = new ArrayList<>();

    public CompilerImpl(Path sourceFolder, Path outputFolder) {
        this.projectStructure = new ProjectStructure(sourceFolder);
        this.outputFolder = outputFolder;
    }

    @Override
    public Messager getMessager() {
        return messager;
    }

    @Override
    public McModMetadata getMetadata() {
        return metadata;
    }

    @Override
    public Path getSourceFolder() {
        return projectStructure.getRoot();
    }

    @Override
    public ProjectStructure getProjectStructure() {
        return projectStructure;
    }

    @Override
    public Path getOutputFolder() {
        return outputFolder;
    }

    @Override
    public String getRootPackageName() {
        return rootPackageName;
    }

    @Override
    public Multimap<ElementType<?>, Element> getElements() {
        return elements;
    }

    @Override
    public ModelManager getModelManager() {
        return modelManager;
    }

    @Override
    public CoffeeMaker getCoffeeMaker() {
        return coffeeMaker;
    }

    @Override
    public Evaluator getEvaluator() {
        return evaluator;
    }

    @Override
    public Filer getClassesFiler() {
        return classesFiler;
    }

    @Override
    public Filer getResourcesFiler() {
        return resourcesFiler;
    }

    @Override
    public Filer getAssetsFiler() {
        return assetsFiler;
    }

    public void run() {
        doInitialize();
        doCompile();
    }

    private void doInitialize() {
        try {
            FileUtils.createDirectoriesIfNotExists(getOutputFolder());

            metadata = JsonUtils.readJson(getSourceFolder().resolve(McModMetadata.FILE_NAME), McModMetadata.class);

            rootPackageName = "peach.generated." + getMetadata().getId();

            elements = loadElements();

            modelManager = new ModelManager();

            coffeeMaker = new CoffeeMaker();
            coffeeMaker.loadTemplateFromJar(Paths.get("template/peach-forge-template-1.0.jar"));
            evaluator = new NashornEvaluator();
            evaluator.getEnv().put("metadata", metadata);

            classesFiler = new Filer(getOutputFolder().resolve("classes"));
            resourcesFiler = new Filer(getOutputFolder().resolve("resources"));
            assetsFiler = new Filer(getResourcesFiler().getRoot().resolve("assets/" + getMetadata().getId()));

            taskList.add(new CleanTask());
            taskList.add(new ElementTask());
            taskList.add(new LanguageTask());
            taskList.add(new MainClassTask());
            taskList.add(new ModInfoTask());
            taskList.add(new AssetsInfoTask());

            Path outputFile = getOutputFolder().resolve("artifacts/" + metadata.getId() + "-" + metadata.getVersion() + ".jar");
            taskList.add(new ZipTask(outputFile, classesFiler.getRoot(), resourcesFiler.getRoot()));
        } catch (Exception e) {
            getMessager().error("Caught an exception when initializing compiler.", e);
        }
    }

    private Multimap<ElementType<?>, Element> loadElements() throws IOException {
        Path sources = getProjectStructure().getSources();

        Multimap<ElementType<?>, Element> elements = HashMultimap.create();

        Iterator<Path> iterator = Files.walk(sources)
                .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json"))
                .iterator();
        while (iterator.hasNext()) {
            Path file = iterator.next();
            ElementType<?> elementType = elementRegistry.getElementType(file);
            elements.put(elementType, loadElement(file));
        }
        return elements;
    }

    @SuppressWarnings("unchecked")
    private Element loadElement(Path file) {
        ElementType<?> type = elementRegistry.getElementType(file);
        if (type == null) {
            throw new IllegalArgumentException("Cannot load element");
        }
        try {
            Element element = JsonUtils.readJson(file, type.getType());
            Element.setFile(element, file);
            return element;
        } catch (IOException e) {
            return type.newInstance(file);
        }
    }


    private void doCompile() {
        try {
            for (CompileTask compileTask : taskList) {
                compileTask.run(this);
            }
        } catch (Exception e) {
            getMessager().error("Caught an exception in executing task.", e);
        }
    }
}
