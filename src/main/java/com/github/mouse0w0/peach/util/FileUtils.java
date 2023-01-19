package com.github.mouse0w0.peach.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

public class FileUtils {
    public static final CopyOption[] EMPTY_COPY_OPTION_ARRAY = new CopyOption[0];
    public static final OpenOption[] EMPTY_OPEN_OPTION_ARRAY = new OpenOption[0];

    public static final CopyOption[] REPLACE_EXISTING = {StandardCopyOption.REPLACE_EXISTING};

    public static final Pattern FILE_NAME_PATTERN = Pattern.compile(
            "[^\\s\\\\/:*?\"<>|](\\x20|[^\\s\\\\/:*?\"<>|])*[^\\s\\\\/:*?\"<>|]$");

    public static final Pattern FILE_NAME_WITHOUT_EXTENSION = Pattern.compile(
            "[^\\s\\\\/:*?\"<>|.](\\x20|[^\\s\\\\/:*?\"<>|.])*[^\\s\\\\/:*?\"<>|.]$");

    public static boolean validateFileName(String fileName) {
        if (fileName == null || fileName.length() > 255) return false;
        return FILE_NAME_PATTERN.matcher(fileName).matches();
    }

    public static boolean validateFileNameWithoutExtension(String fileName) {
        if (fileName == null || fileName.length() > 255) return false;
        return FILE_NAME_WITHOUT_EXTENSION.matcher(fileName).matches();
    }

    public static void createDirectoriesIfNotExists(Path path) throws UncheckedIOException {
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new UncheckedIOException(e.getMessage(), e);
            }
        }
    }

    public static void createFileIfNotExists(Path path) throws UncheckedIOException {
        if (Files.notExists(path)) {
            try {
                Path parent = path.getParent();
                if (Files.notExists(parent)) {
                    Files.createDirectories(parent);
                }
                Files.createFile(path);
            } catch (IOException e) {
                throw new UncheckedIOException(e.getMessage(), e);
            }
        }
    }

    public static void createParentIfNotExists(Path path) throws UncheckedIOException {
        Path parent = path.getParent();
        if (Files.notExists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new UncheckedIOException(e.getMessage(), e);
            }
        }
    }

    public static Path copySafely(Path source, Path target, CopyOption... options) throws UncheckedIOException {
        createParentIfNotExists(target);
        try {
            return Files.copy(source, target, options);
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static long copySafely(InputStream in, Path target, CopyOption... options) throws UncheckedIOException {
        createParentIfNotExists(target);
        try {
            return Files.copy(in, target, options);
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static Path copyIfNotExists(Path source, Path target, CopyOption... options) throws UncheckedIOException {
        if (Files.exists(target)) return target;
        createParentIfNotExists(target);
        try {
            return Files.copy(source, target, options);
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static long copyIfNotExists(InputStream in, Path target, CopyOption... options) throws UncheckedIOException {
        if (Files.exists(target)) return 0;
        createParentIfNotExists(target);
        try {
            return Files.copy(in, target, options);
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static long forceCopy(InputStream in, Path target) throws UncheckedIOException {
        createParentIfNotExists(target);
        try {
            return Files.copy(in, target, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static Path forceCopy(Path source, Path target) throws UncheckedIOException {
        createParentIfNotExists(target);
        try {
            return Files.copy(source, target, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static boolean isEmptyDirectory(Path path) throws UncheckedIOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            return !stream.iterator().hasNext();
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static boolean notEmptyDirectory(Path path) throws UncheckedIOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            return stream.iterator().hasNext();
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static String getFileName(Path path) {
        return path.getFileName().toString();
    }

    public static String getFileNameWithoutExtension(Path path) {
        return StringUtils.substringBeforeLast(path.getFileName().toString(), '.');
    }

    public static String getFileNameWithoutExtension(File file) {
        return StringUtils.substringBeforeLast(file.getName(), '.');
    }

    public static String getFileNameWithoutExtension(String fileName) {
        return StringUtils.substringBeforeLast(fileName, '.');
    }

    public static String getFileExtension(Path path) {
        return StringUtils.substringAfterLast(path.getFileName().toString(), '.');
    }

    public static String getFileExtension(File file) {
        return StringUtils.substringAfterLast(file.getName(), '.');
    }

    public static String getFileExtension(String fileName) {
        return StringUtils.substringAfterLast(fileName, '.');
    }

    public static void delete(Path path) throws UncheckedIOException {
        if (Files.isDirectory(path)) {
            deleteDirectory(path);
        } else {
            deleteFile(path);
        }
    }

    public static void delete(File file) throws UncheckedIOException {
        delete(file.toPath());
    }

    public static void deleteDirectory(Path path) throws UncheckedIOException {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        Files.delete(file);
                    } catch (NoSuchFileException ignored) {
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    try {
                        Files.delete(dir);
                    } catch (NoSuchFileException ignored) {
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static void deleteDirectory(File file) throws UncheckedIOException {
        deleteDirectory(file.toPath());
    }

    public static void deleteFile(Path path) throws UncheckedIOException {
        try {
            Files.delete(path);
        } catch (NoSuchFileException ignored) {
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    public static void deleteFile(File file) throws UncheckedIOException {
        deleteFile(file.toPath());
    }

    public static Path getDirectory(Path path) {
        return Files.isDirectory(path) ? path : path.getParent();
    }

    public static Path toPath(File file) {
        return file != null ? file.toPath() : null;
    }

    public static File toFile(Path path) {
        return path != null ? path.toFile() : null;
    }

    public static Path toPath(URL url) throws IllegalArgumentException {
        try {
            return Path.of(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static File toFile(URL url) throws IllegalArgumentException {
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static URL toURL(Path path) throws IllegalArgumentException {
        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static URL toURL(File file) throws IllegalArgumentException {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static String toURLString(Path path) {
        return URLDecoder.decode(toURL(path).toExternalForm(), StandardCharsets.UTF_8);
    }

    public static String toURLString(File file) {
        return URLDecoder.decode(toURL(file).toExternalForm(), StandardCharsets.UTF_8);
    }
}
