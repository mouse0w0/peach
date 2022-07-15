package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.mcmod.compiler.task.Delete;
import com.github.mouse0w0.peach.mcmod.compiler.task.Task;
import com.github.mouse0w0.peach.mcmod.compiler.task.Zip;
import com.github.mouse0w0.peach.mcmod.compiler.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.*;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.McModDescriptor;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public final class Compiler implements Context {

    private final Project project;

    private ProjectStructure projectStructure;
    private McModMetadata metadata;
    private String id;
    private Path outputFolder;
    private ModelManager modelManager;
    private ElementRegistry elementRegistry;
    private ElementManager elementManager;
    private Configuration templateManager;
    private Messager messager;

    private String rootPackageName;

    private Multimap<ElementType<?>, Element> elements;

    private Filer classesFiler;
    private Filer resourcesFiler;
    private Filer assetsFiler;

    private final List<Task> tasks = new ArrayList<>();

    public Compiler(Project project) {
        this.project = project;
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
    public ProjectStructure getProjectStructure() {
        return projectStructure;
    }

    @Override
    public Path getOutputFolder() {
        return outputFolder;
    }

    @Override
    public Multimap<ElementType<?>, Element> getElements() {
        return elements;
    }

    @Override
    public <T extends Element> Collection<T> getElements(ElementType<T> type) {
        return (Collection<T>) elements.get(type);
    }

    @Override
    public ModelManager getModelManager() {
        return modelManager;
    }

    @Override
    public Configuration getTemplateManager() {
        return templateManager;
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
    public String getRootPackageName() {
        return rootPackageName;
    }

    @Override
    public String getNamespace() {
        return metadata.getId();
    }

    @Override
    public String getInternalName(String className) {
        return ASMUtils.getInternalName(rootPackageName, className);
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
            this.projectStructure = new ProjectStructure(project.getPath());
            this.metadata = McModDescriptor.getInstance(project).getMetadata();
            this.id = metadata.getId();
            this.outputFolder = project.getPath().resolve("build");
            this.modelManager = ModelManager.getInstance();
            this.elementRegistry = ElementRegistry.getInstance();
            this.elementManager = ElementManager.getInstance(project);
            this.templateManager = new Configuration(Configuration.VERSION_2_3_31);
            this.templateManager.setDefaultEncoding("UTF-8");
            this.templateManager.setLocale(Locale.US);
            this.templateManager.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            this.templateManager.setClassLoaderForTemplateLoading(ClassLoader.getSystemClassLoader(), "template");
            this.messager = new MessagerImpl();

            FileUtils.createDirectoriesIfNotExists(getOutputFolder());

            rootPackageName = "peach.generated." + getMetadata().getId();

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

    private Multimap<ElementType<?>, Element> loadElements() throws IOException {
        Path sources = getProjectStructure().getSources();

        Multimap<ElementType<?>, Element> elements = HashMultimap.create();

        Files.walk(sources)
                .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json"))
                .forEach(file -> {
                    ElementType<?> elementType = elementRegistry.getElementType(file);
                    elements.put(elementType, elementManager.loadElement(file));
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
