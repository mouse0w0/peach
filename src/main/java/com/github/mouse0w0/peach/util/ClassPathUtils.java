package com.github.mouse0w0.peach.util;

import org.apache.commons.lang3.function.FailableConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

public final class ClassPathUtils {
    public static URL getResource(String name) throws ResourceNotFoundException {
        return getResource(name, Thread.currentThread().getContextClassLoader());
    }

    public static URL getResource(String name, ClassLoader classLoader) throws ResourceNotFoundException {
        URL resource = classLoader.getResource(name);
        if (resource == null) throw new ResourceNotFoundException(name);
        return resource;
    }

    public static InputStream getResourceAsStream(String name) throws ResourceNotFoundException {
        return getResourceAsStream(name, Thread.currentThread().getContextClassLoader());
    }

    public static InputStream getResourceAsStream(String name, ClassLoader classLoader) throws ResourceNotFoundException {
        InputStream stream = classLoader.getResourceAsStream(name);
        if (stream == null) throw new ResourceNotFoundException(name);
        return stream;
    }

    public static void forEachResources(String name, FailableConsumer<Path, IOException> consumer) throws IOException {
        forEachResources(name, Thread.currentThread().getContextClassLoader(), consumer);
    }

    public static void forEachResources(String name, ClassLoader classLoader, FailableConsumer<Path, IOException> consumer) throws IOException {
        Enumeration<URL> resources = classLoader.getResources(name);
        while (resources.hasMoreElements()) {
            URI uri;
            try {
                uri = resources.nextElement().toURI();
            } catch (URISyntaxException e) {
                continue;
            }

            if ("file".equalsIgnoreCase(uri.getScheme())) {
                forEachResources(FileSystems.getDefault().provider().getPath(uri), consumer);
            } else {
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    forEachResources(fileSystem.provider().getPath(uri), consumer);
                }
            }
        }
    }

    private static void forEachResources(Path dir, FailableConsumer<Path, IOException> consumer) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
            for (Path path : dirStream) {
                consumer.accept(path);
            }
        }
    }

    public static void walk(String name, FileVisitor<? super Path> visitor) throws IOException {
        walk(name, Thread.currentThread().getContextClassLoader(), Collections.emptySet(), Integer.MAX_VALUE, visitor);
    }

    public static void walk(String name, ClassLoader classLoader, FileVisitor<? super Path> visitor) throws IOException {
        walk(name, classLoader, Collections.emptySet(), Integer.MAX_VALUE, visitor);
    }

    public static void walk(String name, Set<FileVisitOption> options, int maxDepth, FileVisitor<? super Path> visitor) throws IOException {
        walk(name, Thread.currentThread().getContextClassLoader(), options, maxDepth, visitor);
    }

    public static void walk(String name, ClassLoader classLoader, Set<FileVisitOption> options, int maxDepth, FileVisitor<? super Path> visitor) throws IOException {
        Enumeration<URL> resources = classLoader.getResources(name);
        while (resources.hasMoreElements()) {
            URI uri;
            try {
                uri = resources.nextElement().toURI();
            } catch (URISyntaxException e) {
                continue;
            }

            if ("file".equalsIgnoreCase(uri.getScheme())) {
                Files.walkFileTree(FileSystems.getDefault().provider().getPath(uri), options, maxDepth, visitor);
            } else {
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    Files.walkFileTree(fileSystem.provider().getPath(uri), options, maxDepth, visitor);
                }
            }
        }
    }

    private ClassPathUtils() {
    }
}
