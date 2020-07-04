package com.github.mouse0w0.peach.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

public class FileUtils {

    public static void createDirectoriesIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    public static void createFileIfNotExists(Path path) throws IOException {
        Path parent = path.getParent();
        if (!Files.exists(path)) {
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }
    }

    public static Stream<Path> walk(Collection<Path> paths, FileVisitOption... options) {
        return paths.stream().flatMap(path -> {
            try {
                return Files.walk(path, options);
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        });
    }

    public static URL toURL(Path path) {
        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new Error(e);
        }
    }

    public static String toURLString(Path path) {
        return toURL(path).toString();
    }
}
