package com.github.mouse0w0.peach.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class JarUtils {
    public static void jar(Path outputFile, Path... sources) throws IOException {
        jar(outputFile, Arrays.asList(sources));
    }

    public static void jar(Path outputFile, Collection<Path> sources) throws IOException {
        FileUtils.createFileIfNotExists(outputFile);
        try (OutputStream outputStream = Files.newOutputStream(outputFile);
             JarOutputStream output = new JarOutputStream(outputStream)) {
            for (Path source : sources) {
                Path root = Files.isRegularFile(source) ? source.getParent() : source;
                Iterator<Path> iterator = Files.walk(source).iterator();
                while (iterator.hasNext()) {
                    Path file = iterator.next();

                    if (Files.isRegularFile(file)) {
                        copyIntoJar(file, root.relativize(file).toString().replace('\\', '/'), output);
                    }
                }
            }
        }
    }

    private static void copyIntoJar(Path file, String entryName, JarOutputStream output) throws IOException {
        try (InputStream input = Files.newInputStream(file)) {
            output.putNextEntry(new JarEntry(entryName));
            IOUtils.copy(input, output);
        }
    }
}
