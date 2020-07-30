package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.data.DataHolderImpl;
import com.github.mouse0w0.peach.data.Key;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.AssetsInfoTask;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.ElementTask;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.MainClassTask;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.ModInfoTask;
import com.github.mouse0w0.peach.mcmod.data.McModSettings;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CompilerImpl extends DataHolderImpl implements CompileContext {

    public static final Key<McModSettings> MOD_INFO_KEY = Key.of(McModSettings.class);

    public static final Key<Path> PROJECT_SOURCES_PATH = Key.of("ProjectSourcesPath");
    public static final Key<Path> PROJECT_RESOURCES_PATH = Key.of("ProjectResourcesPath");

    public static final Key<Filer> MOD_ASSETS_FILER = Key.of("ModAssetsFiler");

    public static final Key<String> ROOT_PACKAGE_NAME = Key.of("RootPackageName");

    private final Path sourceDirectory;
    private final Path outputDirectory;

    private Filer classesFiler;
    private Filer resourcesFiler;

    private final List<CompileTask> taskList = new ArrayList<>();

    public CompilerImpl(Path sourceDirectory, Path outputDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.outputDirectory = outputDirectory;
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
    public Filer getClassesFiler() {
        return classesFiler;
    }

    @Override
    public Filer getResourcesFiler() {
        return resourcesFiler;
    }

    public void run() {
        doInitialize();
        doCompile();
    }

    private void doInitialize() {
        try {
            FileUtils.createDirectoriesIfNotExists(getOutputDirectory());

            Path projectSourcesPath = getSourceDirectory().resolve("sources");
            putData(PROJECT_SOURCES_PATH, projectSourcesPath);

            Path projectResourcesPath = getSourceDirectory().resolve("resources");
            putData(PROJECT_RESOURCES_PATH, projectResourcesPath);

            classesFiler = new Filer(getOutputDirectory().resolve("classes"));

            Path resourcesStorePath = getOutputDirectory().resolve("resources");
            resourcesFiler = new Filer(resourcesStorePath);

            McModSettings modInfo = JsonUtils.readJson(getSourceDirectory().resolve(McModSettings.FILE_NAME), McModSettings.class);
            putData(MOD_INFO_KEY, modInfo);
            putData(ROOT_PACKAGE_NAME, "peach.generated." + modInfo.getId());
            putData(MOD_ASSETS_FILER, new Filer(resourcesStorePath.resolve("assets/" + modInfo.getId())));

            taskList.add(new ElementTask());
            taskList.add(new MainClassTask());
            taskList.add(new ModInfoTask());
            taskList.add(new AssetsInfoTask());

            taskList.add(new JarTask(getOutputDirectory().resolve("artifacts")));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
