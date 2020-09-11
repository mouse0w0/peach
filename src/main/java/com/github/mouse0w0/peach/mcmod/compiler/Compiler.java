package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.data.Key;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.*;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.McModSettings;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Compiler implements Environment {

    public static final Key<McModSettings> MOD_INFO_KEY = Key.of(McModSettings.class);

    public static final Key<Path> PROJECT_SOURCES_PATH = Key.of("ProjectSourcesPath");
    public static final Key<Path> PROJECT_RESOURCES_PATH = Key.of("ProjectResourcesPath");

    public static final Key<Filer> MOD_ASSETS_FILER = Key.of("ModAssetsFiler");

    public static final Key<String> ROOT_PACKAGE_NAME = Key.of("RootPackageName");

    private final Path sourceDirectory;
    private final Path outputDirectory;

    private McModSettings modSettings;

    private String rootPackageName;

    private Multimap<ElementType<?>, Element<?>> elements;

    private ModelManager modelManager;

    private Filer classesFiler;
    private Filer resourcesFiler;
    private Filer assetsFiler;

    private final List<CompileTask> taskList = new ArrayList<>();

    public Compiler(Path sourceDirectory, Path outputDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public McModSettings getModSettings() {
        return modSettings;
    }

    @Override
    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    @Override
    public Path getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    public String getRootPackageName() {
        return rootPackageName;
    }

    @Override
    public Multimap<ElementType<?>, Element<?>> getElements() {
        return elements;
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

    public void run() {
        doInitialize();
        doCompile();
    }

    private void doInitialize() {
        try {
            FileUtils.createDirectoriesIfNotExists(getOutputDirectory());

            modSettings = JsonUtils.readJson(getSourceDirectory().resolve(McModSettings.FILE_NAME), McModSettings.class);

            rootPackageName = "peach.generated." + getModSettings().getId();

            elements = loadElements();

            modelManager = new ModelManager();

            classesFiler = new Filer(getOutputDirectory().resolve("classes"));
            resourcesFiler = new Filer(getOutputDirectory().resolve("resources"));
            assetsFiler = new Filer(getResourcesFiler().getRoot().resolve("assets/" + getModSettings().getId()));

            taskList.add(new ElementTask());
            taskList.add(new LanguageTask());
            taskList.add(new MainClassTask());
            taskList.add(new ModInfoTask());
            taskList.add(new AssetsInfoTask());

            Path outputFile = getOutputDirectory().resolve("artifacts/" + modSettings.getId() + "-" + modSettings.getVersion() + ".jar");
            taskList.add(new ZipTask(outputFile, classesFiler.getRoot(), resourcesFiler.getRoot()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Multimap<ElementType<?>, Element<?>> loadElements() throws IOException {
        Path sources = getSourceDirectory().resolve("sources");
        ElementManager elementManager = ElementManager.getInstance();

        Multimap<ElementType<?>, Element<?>> elements = HashMultimap.create();

        Iterator<Path> iterator = Files.walk(sources)
                .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json"))
                .iterator();
        while (iterator.hasNext()) {
            Path file = iterator.next();
            ElementType<?> element = elementManager.getElement(file);
            Element<?> elementFile = element.load(file);
            elementFile.load();
            elements.put(element, elementFile);
        }
        return elements;
    }

    private void doCompile() {
        try {
            for (CompileTask compileTask : taskList) {
                compileTask.run(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
