package com.github.mouse0w0.peach.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static void zip(Path outputFile, Path... sources) throws IOException {
        zip(outputFile, Arrays.asList(sources));
    }

    public static void zip(Path outputFile, Collection<Path> sources) throws IOException {
        FileUtils.createFileIfNotExists(outputFile);
        try (ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(outputFile))) {
            for (Path source : sources) {
                Path root = Files.isRegularFile(source) ? source.getParent() : source;
                Iterator<Path> iterator = Files.walk(source).iterator();
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
