package com.github.mouse0w0.peach.mcmod.compiler.task;

import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.util.FileUtils;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.zip.ZipOutputStream;

public class Zip implements Task {
    private final Path output;
    private final Set<Path> inputs;

    public Zip(Path output, Path... inputs) {
        this.output = output;
        this.inputs = ImmutableSet.copyOf(inputs);
    }

    public Path getOutput() {
        return output;
    }

    public Set<Path> getInputs() {
        return inputs;
    }

    @Override
    public void run(Context context) throws Exception {
        FileUtils.createFileIfNotExists(output);
        try (ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(this.output))) {
            for (Path input : inputs) {
                Path root = Files.isRegularFile(input) ? input.getParent() : input;
                Iterator<Path> iterator = Files.walk(input).iterator();
                while (iterator.hasNext()) {
                    Path file = iterator.next();

                    if (Files.isRegularFile(file)) {
                        copyFileIntoZip(file, root.relativize(file).toString().replace('\\', '/'), output);
                    }
                }
            }
        }
    }

    private static void copyFileIntoZip(Path file, String entryName, ZipOutputStream output) throws IOException {
        try (InputStream input = Files.newInputStream(file)) {
            output.putNextEntry(new JarEntry(entryName));
            IOUtils.copy(input, output);
        }
    }
}
