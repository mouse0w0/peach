package com.github.mouse0w0.peach.util;

import com.github.mouse0w0.peach.exception.RuntimeIOException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
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

    public static void createParentIfNotExists(Path path) throws IOException {
        Path parent = path.getParent();
        if (!Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    public static Path copyIfNotExists(Path source, Path target, CopyOption... options) throws IOException {
        if (Files.exists(target) && !ArrayUtils.contains(options, StandardCopyOption.REPLACE_EXISTING)) return target;
        createParentIfNotExists(target);
        return Files.copy(source, target, options);
    }

    public static long copyIfNotExists(InputStream in, Path target, CopyOption... options) throws IOException {
        if (Files.exists(target) && !ArrayUtils.contains(options, StandardCopyOption.REPLACE_EXISTING)) return 0;
        createParentIfNotExists(target);
        return Files.copy(in, target, options);
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
            throw new IllegalStateException(e);
        }
    }

    public static String toURLString(Path path) {
        return toURL(path).toString();
    }

    public static Path toPath(URL url) {
        try {
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean isEmpty(Path path) throws IOException {
        return Files.list(path).count() == 0;
    }

    public static boolean isNotEmpty(Path path) throws IOException {
        return Files.list(path).count() > 0;
    }

    public static String getFileNameWithoutExtensionName(Path file) {
        return StringUtils.substringBefore(file.getFileName().toString(), '.');
    }

    public static String getFileNameWithoutExtensionName(File file) {
        return StringUtils.substringBefore(file.getName(), '.');
    }
}
