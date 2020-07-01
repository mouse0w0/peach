package com.github.mouse0w0.peach.forge.generator;

import com.github.mouse0w0.peach.data.DataHolderImpl;
import com.github.mouse0w0.peach.data.Key;
import com.github.mouse0w0.peach.forge.ForgeProjectInfo;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ForgeCompiler extends DataHolderImpl implements CompileContext {

    public static final Key<ForgeProjectInfo> PROJECT_INFO_KEY = Key.of(ForgeProjectInfo.class);

    public static final Key<Path> CLASSES_STORE_PATH = Key.of("ClassesStorePath");
    public static final Key<Path> RESOURCES_STORE_PATH = Key.of("ResourcesStorePath");
    public static final Key<Path> ARTIFACTS_STORE_PATH = Key.of("ArtifactsStorePath");

    public static final Key<String> ROOT_PACKAGE_NAME = Key.of("RootPackageName");

    private final Path source;
    private final Path output;

    public ForgeCompiler(Path source, Path output) {
        this.source = source;
        this.output = output;
    }

    @Override
    public Path getSource() {
        return source;
    }

    @Override
    public Path getOutput() {
        return output;
    }

    @Override
    public OutputStream newOutputStream(String path) throws IOException {
        return newOutputStream(getOutput().resolve(path));
    }

    @Override
    public OutputStream newOutputStream(Path path) throws IOException {
        FileUtils.createFileIfNotExists(path);
        return Files.newOutputStream(path);
    }

    @Override
    public Writer newWriter(String path) throws IOException {
        return newWriter(getOutput().resolve(path));
    }

    @Override
    public Writer newWriter(Path path) throws IOException {
        return newWriter(path, StandardCharsets.UTF_8);
    }

    @Override
    public Writer newWriter(Path path, Charset charset) throws IOException {
        FileUtils.createFileIfNotExists(path);
        return Files.newBufferedWriter(path, charset);
    }

    @Override
    public void write(Path path, byte[] bytes) throws IOException {
        FileUtils.createFileIfNotExists(path);
        Files.write(path, bytes);
    }

    @Override
    public void write(Path path, String... lines) throws IOException {
        write(path, StandardCharsets.UTF_8, lines);
    }

    @Override
    public void write(Path path, Charset charset, String... lines) throws IOException {
        FileUtils.createFileIfNotExists(path);
        try (Writer writer = newWriter(path, charset)) {
            for (String line : lines) {
                writer.append(line).append('\n');
            }
        }
    }

    public void run() {
        doCompile();
    }

    private void doCompile() {
        try {
            FileUtils.createDirectoriesIfNotExists(getOutput());

            Path classesStorePath = getOutput().resolve("classes");
            FileUtils.createDirectoriesIfNotExists(classesStorePath);
            putData(CLASSES_STORE_PATH, classesStorePath);

            Path resourcesStorePath = getOutput().resolve("resources");
            FileUtils.createDirectoriesIfNotExists(resourcesStorePath);
            putData(RESOURCES_STORE_PATH, resourcesStorePath);

            Path artifactsStorePath = getOutput().resolve("artifacts");
            FileUtils.createDirectoriesIfNotExists(artifactsStorePath);
            putData(ARTIFACTS_STORE_PATH, artifactsStorePath);

            ForgeProjectInfo projectInfo = JsonUtils.readJson(getSource().resolve(ForgeProjectInfo.FILE_NAME), ForgeProjectInfo.class);
            putData(PROJECT_INFO_KEY, projectInfo);

            putData(ROOT_PACKAGE_NAME, "peach.generated." + projectInfo.getId());

            MainClassGenerator.generate(this);
            ModInfoGenerator.generate(this);

            JarGenerator.generate(this);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
