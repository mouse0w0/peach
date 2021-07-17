package com.github.mouse0w0.peach.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

    public static final CopyOption[] REPLACE_EXISTING = {StandardCopyOption.REPLACE_EXISTING};

    public static final Pattern FILE_NAME_PATTERN = Pattern.compile(
            "[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|]$");

    public static final Pattern FILE_NAME_WITHOUT_EXTENSION = Pattern.compile(
            "[^\\s\\\\/:\\*\\?\\\"<>\\|\\.](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|\\.])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");

    public static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255) return false;
        return FILE_NAME_PATTERN.matcher(fileName).matches();
    }

    public static boolean isValidFileNameWithoutExtension(String fileName) {
        if (fileName == null || fileName.length() > 255) return false;
        return FILE_NAME_WITHOUT_EXTENSION.matcher(fileName).matches();
    }

    public static void createDirectoriesIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    public static void createDirectoriesIfNotExistsSilently(Path path) throws UncheckedIOException {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
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

    public static long forceCopySilently(InputStream in, Path target) throws UncheckedIOException {
        try (InputStream inputStream = in) {
            return forceCopy(inputStream, target);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Path forceCopy(Path source, Path target) throws IOException {
        createParentIfNotExists(target);
        return Files.copy(source, target, REPLACE_EXISTING);
    }

    public static Path forceCopySilently(Path source, Path target) throws UncheckedIOException {
        try {
            return forceCopy(source, target);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static final Pattern URL_TRIPLET = Pattern.compile("%[a-fA-F0-9]{2}");

    public static String toFileSystemUrlAsString(Path path) {
        String url = toUrlAsString(path);
        Matcher matcher = URL_TRIPLET.matcher(url);
        StringBuilder result = new StringBuilder(url.length());
        int offset = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            result.append(url, offset, start)
                    .append(parseChar(url, start + 1, end));
            offset = end;
        }
        result.append(url, offset, url.length());
        return result.toString();
    }

    private static String toUrlAsString(Path path) {
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

    public static boolean isEmpty(Path path) {
        try {
            return !Files.list(path).findFirst().isPresent();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static boolean isNotEmpty(Path path) {
        try {
            return Files.list(path).findFirst().isPresent();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String getFileName(Path path) {
        return path.getFileName().toString();
    }

    public static String getFileNameWithoutExt(Path path) {
        return StringUtils.substringBeforeLast(path.getFileName().toString(), '.');
    }

    public static String getFileNameWithoutExt(File file) {
        return StringUtils.substringBeforeLast(file.getName(), '.');
    }

    public static String getFileNameWithoutExt(String fileName) {
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

    public static boolean delete(Path path) {
        return delete(path.toFile());
    }

    public static boolean delete(File file) {
        if (!file.exists()) {
            return false;
        }
        return file.isFile() ? file.delete() : deleteDirectory(file);
    }

    public static boolean deleteDirectoryIfPresent(Path path) {
        return deleteDirectoryIfPresent(path.toFile());
    }

    public static boolean deleteDirectoryIfPresent(File file) {
        if (!file.exists()) {
            return false;
        }
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

    public static Path toPath(File file) {
        return file != null ? file.toPath() : null;
    }

    public static File toFile(Path path) {
        return path != null ? path.toFile() : null;
    }
}
