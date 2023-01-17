package com.github.mouse0w0.peach.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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

    public static void createDirectoriesIfNotExists(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }

    public static void createFileIfNotExists(Path path) throws IOException {
        if (Files.notExists(path)) {
            Path parent = path.getParent();
            if (Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }
    }

    public static void createParentIfNotExists(Path path) throws IOException {
        Path parent = path.getParent();
        if (Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
    }

    public static Path copySafely(Path source, Path target, CopyOption... options) throws IOException {
        createParentIfNotExists(target);
        return Files.copy(source, target, options);
    }

    public static long copySafely(InputStream in, Path target, CopyOption... options) throws IOException {
        createParentIfNotExists(target);
        return Files.copy(in, target, options);
    }

    public static Path copyIfNotExists(Path source, Path target, CopyOption... options) throws IOException {
        if (Files.exists(target)) return target;
        createParentIfNotExists(target);
        return Files.copy(source, target, options);
    }

    public static long copyIfNotExists(InputStream in, Path target, CopyOption... options) throws IOException {
        if (Files.exists(target)) return 0;
        createParentIfNotExists(target);
        return Files.copy(in, target, options);
    }

    public static long forceCopy(InputStream in, Path target) throws IOException {
        createParentIfNotExists(target);
        return Files.copy(in, target, REPLACE_EXISTING);
    }

    public static Path forceCopy(Path source, Path target) throws IOException {
        createParentIfNotExists(target);
        return Files.copy(source, target, REPLACE_EXISTING);
    }

    public static boolean isEmpty(Path path) throws IOException {
        return !notEmpty(path);
    }

    public static boolean notEmpty(Path path) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            return stream.iterator().hasNext();
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

    public static boolean deleteIfExists(Path path) {
        return deleteIfExists(path.toFile());
    }

    public static boolean deleteIfExists(File file) {
        if (!file.exists()) return false;
        return file.isFile() ? file.delete() : deleteDirectory(file);
    }

    public static boolean deleteDirectoryIfExists(Path path) {
        return deleteDirectoryIfExists(path.toFile());
    }

    public static boolean deleteDirectoryIfExists(File file) {
        if (!file.exists()) return false;
        return deleteDirectory(file);
    }

    public static boolean deleteDirectory(Path path) {
        return deleteDirectory(path.toFile());
    }

    public static boolean deleteDirectory(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File child : files) {
                deleteDirectory(child);
            }
        }
        return file.delete();
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

    public static Path toPath(URL url) {
        try {
            return Path.of(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static File toFile(URL url) {
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL toURL(Path path) {
        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL toURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toURLString(Path path) {
        return URLDecoder.decode(toURL(path).toExternalForm(), StandardCharsets.UTF_8);
    }

    public static String toURLString(File file) {
        return URLDecoder.decode(toURL(file).toExternalForm(), StandardCharsets.UTF_8);
    }
}
