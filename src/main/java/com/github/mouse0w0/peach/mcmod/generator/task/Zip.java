package com.github.mouse0w0.peach.mcmod.generator.task;

import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip implements Task {
    private final Path outputPath;
    private final List<Path> inputPaths = new ArrayList<>();

    public Zip(Path outputPath, Path... inputPaths) {
        this.outputPath = outputPath;
        Collections.addAll(this.inputPaths, inputPaths);
    }

    public Zip(Path outputPath, Collection<Path> inputPaths) {
        this.outputPath = outputPath;
        this.inputPaths.addAll(inputPaths);
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public List<Path> getInputPaths() {
        return inputPaths;
    }

    @Override
    public void run(Context context) throws Exception {
        try (ZipOutputStream output = new ZipOutputStream(FileUtils.newOutputStream(outputPath))) {
            for (Path input : inputPaths) {
                if (Files.isDirectory(input)) {
                    try (Stream<Path> walk = Files.walk(input)) {
                        Iterator<Path> iterator = walk.iterator();
                        while (iterator.hasNext()) {
                            Path file = iterator.next();

                            if (Files.isRegularFile(file)) {
                                putFileIntoZip(input.relativize(file).toString().replace('\\', '/'), file, output);
                            }
                        }
                    }
                } else if (Files.isRegularFile(input)) {
                    putFileIntoZip(FileUtils.getFileName(input), input, output);
                }
            }
        }
    }

    private static void putFileIntoZip(String entryName, Path source, ZipOutputStream output) throws IOException {
        output.putNextEntry(new ZipEntry(entryName));
        Files.copy(source, output);
    }
}
