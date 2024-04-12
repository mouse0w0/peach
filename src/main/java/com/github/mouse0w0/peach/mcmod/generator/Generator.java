package com.github.mouse0w0.peach.mcmod.generator;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.mcmod.element.provider.ElementProvider;
import com.github.mouse0w0.peach.mcmod.generator.task.Delete;
import com.github.mouse0w0.peach.mcmod.generator.task.Task;
import com.github.mouse0w0.peach.mcmod.generator.task.Zip;
import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.*;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.ModProjectMetadata;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Generator implements Context {

    private final Project project;

    private ModProjectMetadata metadata;
    private String id;
    private Path projectFolder;
    private Path sourceFolder;
    private Path resourceFolder;
    private Path modelsFolder;
    private Path texturesFolder;
    private Path outputFolder;
    private ModelManager modelManager;
    private ElementRegistry elementRegistry;
    private ElementManager elementManager;
    private Messager messager;

    private String rootPackage;

    private Multimap<Class<?>, Element> elements;

    private Filer classesFiler;
    private Filer resourcesFiler;
    private Filer assetsFiler;

    private final List<Task> tasks = new ArrayList<>();

    public Generator(Project project) {
        this.project = project;
    }

    @Override
    public Messager getMessager() {
        return messager;
    }

    @Override
    public ModProjectMetadata getMetadata() {
        return metadata;
    }

    @Override
    public Path getProjectFolder() {
        return projectFolder;
    }

    @Override
    public Path getSourceFolder() {
        return sourceFolder;
    }

    @Override
    public Path getResourceFolder() {
        return resourceFolder;
    }

    @Override
    public Path getModelsFolder() {
        return modelsFolder;
    }

    @Override
    public Path getTexturesFolder() {
        return texturesFolder;
    }

    @Override
    public Path getOutputFolder() {
        return outputFolder;
    }

    @Override
    public Multimap<Class<?>, Element> getElements() {
        return elements;
    }

    @Override
    public <T extends Element> Collection<T> getElements(Class<T> type) {
        return (Collection<T>) elements.get(type);
    }

    @Override
    public ModelManager getModelManager() {
        return modelManager;
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

    @Override
    public String getRootPackage() {
        return rootPackage;
    }

    @Override
    public String getNamespace() {
        return metadata.getId();
    }

    @Override
    public String mapIdentifier(Identifier identifier) {
        if (identifier.isProjectNamespace()) {
            return getNamespace() + ":" + identifier.getPath();
        } else {
            return identifier.toString();
        }
    }

    @Override
    public String getInternalName(String className) {
        return ASMUtils.getInternalName(rootPackage, className);
    }

    @Override
    public String getTranslationKey(String prefix, String identifier) {
        return prefix + "." + id + "." + identifier;
    }

    @Override
    public String getTranslationKey(String identifier) {
        return id + "." + identifier;
    }

    @Override
    public String getResourceKey(String prefix, String identifier) {
        return id + ":" + prefix + "/" + identifier;
    }

    public void run() {
        doInitialize();
        doCompile();
    }

    private void doInitialize() {
        try {
            this.projectFolder = project.getPath();
            this.sourceFolder = projectFolder.resolve("sources");
            this.resourceFolder = projectFolder.resolve("resources");
            this.modelsFolder = resourceFolder.resolve("models");
            this.texturesFolder = resourceFolder.resolve("textures");
            this.metadata = ModProjectService.getInstance(project).getMetadata();
            this.id = metadata.getId();
            this.outputFolder = project.getPath().resolve("build");
            this.modelManager = ModelManager.getInstance(project);
            this.elementRegistry = ElementRegistry.getInstance();
            this.elementManager = ElementManager.getInstance(project);
            this.messager = new MessagerImpl();

            FileUtils.createDirectoriesIfNotExists(getOutputFolder());

            rootPackage = "peach.generated." + getMetadata().getId();

            elements = loadElements();

            classesFiler = new Filer(getOutputFolder().resolve("classes"));
            resourcesFiler = new Filer(getOutputFolder().resolve("resources"));
            assetsFiler = new Filer(getResourcesFiler().getRoot().resolve("assets/" + getMetadata().getId()));

            tasks.add(new Delete(getOutputFolder()));
            tasks.add(new GenItemGroup());
            tasks.add(new GenItemGroupConstants());
            tasks.add(new GenBlock());
            tasks.add(new GenItem());
            tasks.add(new GenCraftingRecipe());
            tasks.add(new GenSmeltingRecipe());
            tasks.add(new GenLanguage());
            tasks.add(new GenMainClass());
            tasks.add(new GenMetadata());
            tasks.add(new GenAssetsInfo());
            tasks.add(new Zip(
                    getOutputFolder().resolve("artifacts/" + metadata.getId() + "-" + metadata.getVersion() + ".jar"),
                    classesFiler.getRoot(),
                    resourcesFiler.getRoot()));
        } catch (Exception e) {
            getMessager().error("Caught an exception when initializing compiler.", e);
        }
    }

    private Multimap<Class<?>, Element> loadElements() throws IOException {
        Multimap<Class<?>, Element> elements = HashMultimap.create();

        Files.walk(getSourceFolder())
                .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json"))
                .forEach(file -> {
                    ElementProvider<?> provider = elementRegistry.getElementProvider(file);
                    elements.put(provider.getType(), elementManager.loadElement(file));
                });
        return elements;
    }

    private void doCompile() {
        try {
            for (Task task : tasks) {
                task.run(this);
            }
        } catch (Exception e) {
            getMessager().error("Caught an exception in executing task.", e);
        }
    }
}
