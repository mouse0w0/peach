package com.github.mouse0w0.peach.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileUtils {

    public static final CopyOption[] EMPTY_COPY_OPTION_ARRAY = new CopyOption[0];
    public static final OpenOption[] EMPTY_OPEN_OPTION_ARRAY = new OpenOption[0];

    public static final CopyOption[] REPLACE_EXISTING = {StandardCopyOption.REPLACE_EXISTING};

    public static final Pattern FILE_NAME_PATTERN = Pattern.compile(
            "[^\\s\\\\/:*?\"<>|](\\x20|[^\\s\\\\/:*?\"<>|])*[^\\s\\\\/:*?\"<>|]$");

    public static final Pattern FILE_NAME_WITHOUT_EXTENSION = Pattern.compile(
            "[^\\s\\\\/:*?\"<>|.](\\x20|[^\\s\\\\/:*?\"<>|.])*[^\\s\\\\/:*?\"<>|.]$");

    public static boolean validateFileName(final String fileName) {
        if (fileName == null || fileName.length() > 255) return false;
        return FILE_NAME_PATTERN.matcher(fileName).matches();
    }

    public static boolean validateFileNameWithoutExtension(final String fileName) {
        if (fileName == null || fileName.length() > 255) return false;
        return FILE_NAME_WITHOUT_EXTENSION.matcher(fileName).matches();
    }

    public static void createDirectoriesIfNotExists(final Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }

    public static void createFileIfNotExists(final Path path) throws IOException {
        if (Files.notExists(path)) {
            final Path parent = path.getParent();
            if (Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }
    }

    public static void createParentIfNotExists(final Path path) throws IOException {
        final Path parent = path.getParent();
        if (Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
    }

    public static Path copySafely(final Path source, final Path target, final CopyOption... options) throws IOException {
        createParentIfNotExists(target);
        return Files.copy(source, target, options);
    }

    public static long copySafely(final InputStream in, final Path target, final CopyOption... options) throws IOException {
        createParentIfNotExists(target);
        return Files.copy(in, target, options);
    }

    public static Path copyIfNotExists(final Path source, final Path target, final CopyOption... options) throws IOException {
        if (Files.exists(target)) return target;
        createParentIfNotExists(target);
        return Files.copy(source, target, options);
    }

    public static long copyIfNotExists(final InputStream in, final Path target, final CopyOption... options) throws IOException {
        if (Files.exists(target)) return 0;
        createParentIfNotExists(target);
        return Files.copy(in, target, options);
    }

    public static long forceCopy(final InputStream in, final Path target) throws IOException {
        createParentIfNotExists(target);
        return Files.copy(in, target, REPLACE_EXISTING);
    }

    public static Path forceCopy(final Path source, final Path target) throws IOException {
        createParentIfNotExists(target);
        return Files.copy(source, target, REPLACE_EXISTING);
    }

    public static String toFileSystemUrlAsString(final Path path) {
        String url = toUrlAsString(path);
        StringBuilder result = new StringBuilder(url.length());
        int prev = 0, next;
        while ((next = url.indexOf('%', prev)) != -1) {
            result.append(url, prev, next).append(parseChar(url, next + 1, next + 3));
            prev = next + 1;
        }
        result.append(url, prev, url.length());
        return result.toString();
    }

    private static String toUrlAsString(final Path path) {
        try {
            return path.toUri().toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Failed to convert path to url", e);
        }
    }

    private static char parseChar(String s, int start, int end) {
        int result = 0;
        for (int i = start; i < end; i++) {
            result = Character.digit(s.charAt(i), 16) + (result << 4);
        }
        return (char) result;
    }

    public static boolean isEmpty(final Path path) throws IOException {
        return !notEmpty(path);
    }

    public static boolean notEmpty(final Path path) throws IOException {
        try (Stream<Path> stream = Files.list(path)) {
            return stream.findFirst().isPresent();
        }
    }

    public static String getFileName(final Path path) {
        return path.getFileName().toString();
    }

    public static String getFileNameWithoutExtension(final Path path) {
        return StringUtils.substringBeforeLast(path.getFileName().toString(), '.');
    }

    public static String getFileNameWithoutExtension(final File file) {
        return StringUtils.substringBeforeLast(file.getName(), '.');
    }

    public static String getFileNameWithoutExtension(final String fileName) {
        return StringUtils.substringBeforeLast(fileName, '.');
    }

    public static String getFileExtension(final Path path) {
        return StringUtils.substringAfterLast(path.getFileName().toString(), '.');
    }

    public static String getFileExtension(final File file) {
        return StringUtils.substringAfterLast(file.getName(), '.');
    }

    public static String getFileExtension(final String fileName) {
        return StringUtils.substringAfterLast(fileName, '.');
    }

    public static boolean deleteIfExists(final Path path) {
        return deleteIfExists(path.toFile());
    }

    public static boolean deleteIfExists(final File file) {
        if (!file.exists()) return false;
        return file.isFile() ? file.delete() : deleteDirectory(file);
    }

    public static boolean deleteDirectoryIfExists(final Path path) {
        return deleteDirectoryIfExists(path.toFile());
    }

    public static boolean deleteDirectoryIfExists(final File file) {
        if (!file.exists()) return false;
        return deleteDirectory(file);
    }

    public static boolean deleteDirectory(final Path path) {
        return deleteDirectory(path.toFile());
    }

    public static boolean deleteDirectory(final File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File child : files) {
                deleteDirectory(child);
            }
        }
        return file.delete();
    }

    public static Path getDirectory(final Path path) {
        return Files.isDirectory(path) ? path : path.getParent();
    }

    public static Path toPath(final File file) {
        return file != null ? file.toPath() : null;
    }

    public static File toFile(final Path path) {
        return path != null ? path.toFile() : null;
    }
}
