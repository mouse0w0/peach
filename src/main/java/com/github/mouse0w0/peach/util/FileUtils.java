package com.github.mouse0w0.peach.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileUtils {
    public static final FileAttribute<?>[] EMPTY_FILE_ATTRIBUTE_ARRAY = new FileAttribute[0];

    public static final CopyOption[] EMPTY_COPY_OPTION_ARRAY = new CopyOption[0];
    public static final OpenOption[] EMPTY_OPEN_OPTION_ARRAY = new OpenOption[0];
    public static final LinkOption[] EMPTY_LINK_OPTION_ARRAY = new LinkOption[0];

    public static final CopyOption[] REPLACE_EXISTING = {StandardCopyOption.REPLACE_EXISTING};
    public static final LinkOption[] NOFOLLOW_LINKS = {LinkOption.NOFOLLOW_LINKS};

    public static final Pattern FILE_NAME_PATTERN = Pattern.compile(
            "^(?!(?:CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\.[^.]*)*$)" +
                    "[^<>:\"/\\\\|?*\\x00-\\x1F]*[^<>:\"/\\\\|?*\\x00-\\x1F\\s.]$",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern FILE_NAME_WITHOUT_EXTENSION = Pattern.compile(
            "^(?!(?:CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])$)" +
                    "[^<>:\"/\\\\|?*\\x00-\\x1F.]*[^<>:\"/\\\\|?*\\x00-\\x1F\\s.]$",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static boolean validateFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) return false;
        return FILE_NAME_PATTERN.matcher(fileName).matches();
    }

    public static boolean validateFileNameWithoutExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) return false;
        return FILE_NAME_WITHOUT_EXTENSION.matcher(fileName).matches();
    }

    public static Path createDirectories(Path dir) throws UncheckedIOException {
        try {
            return Files.createDirectories(dir, EMPTY_FILE_ATTRIBUTE_ARRAY);
        } catch (IOException e) {
            throw unchecked(e);
        }
    }

    public static Path ensureFileExists(Path path) throws UncheckedIOException {
        if (Files.notExists(path)) {
            try {
                Files.createFile(ensureParentExists0(path), EMPTY_FILE_ATTRIBUTE_ARRAY);
            } catch (IOException e) {
                throw unchecked(e);
            }
        }
        return path;
    }

    public static Path ensureParentExists(Path path) throws UncheckedIOException {
        try {
            return ensureParentExists0(path);
        } catch (IOException e) {
            throw unchecked(e);
        }
    }

    private static Path ensureParentExists0(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent, EMPTY_FILE_ATTRIBUTE_ARRAY);
        }
        return path;
    }

    public static OutputStream newOutputStream(Path path) throws IOException {
        return Files.newOutputStream(ensureParentExists0(path), EMPTY_OPEN_OPTION_ARRAY);
    }

    public static BufferedWriter newBufferedWriter(Path path) throws IOException {
        return newBufferedWriter(path, StandardCharsets.UTF_8);
    }

    public static BufferedWriter newBufferedWriter(Path path, Charset charset) throws IOException {
        return Files.newBufferedWriter(ensureParentExists0(path), charset, EMPTY_OPEN_OPTION_ARRAY);
    }

    public static Path copyIfNotExists(Path source, Path target) throws UncheckedIOException {
        return copyIfNotExists(source, target, EMPTY_COPY_OPTION_ARRAY);
    }

    public static Path copyIfNotExists(Path source, Path target, CopyOption... options) throws UncheckedIOException {
        if (Files.notExists(source)) {
            throw unchecked(new NoSuchFileException(source.toString()));
        }
        if (Files.notExists(target)) {
            try {
                return Files.copy(source, ensureParentExists0(target), options);
            } catch (IOException e) {
                throw unchecked(e);
            }
        }
        return target;
    }

    public static Path forceCopy(Path source, Path target) throws UncheckedIOException {
        if (Files.notExists(source)) {
            throw unchecked(new NoSuchFileException(source.toString()));
        }
        try {
            return Files.copy(source, ensureParentExists0(target), REPLACE_EXISTING);
        } catch (IOException e) {
            throw unchecked(e);
        }
    }

    public static boolean isEmptyDirectory(Path path) throws UncheckedIOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            return !stream.iterator().hasNext();
        } catch (IOException e) {
            throw unchecked(e);
        }
    }

    public static boolean notEmptyDirectory(Path path) throws UncheckedIOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            return stream.iterator().hasNext();
        } catch (IOException e) {
            throw unchecked(e);
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

    private static final FileVisitor<Path> DELETE_FILE_VISITOR = new FileVisitor<>() {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            try {
                Files.delete(file);
            } catch (NoSuchFileException ignored) {
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            throw exc;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            try {
                Files.delete(dir);
            } catch (NoSuchFileException ignored) {
            }
            return FileVisitResult.CONTINUE;
        }
    };

    public static void deleteDirectory(Path path) throws UncheckedIOException {
        try {
            Files.walkFileTree(path, DELETE_FILE_VISITOR);
        } catch (IOException e) {
            throw unchecked(e);
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
            throw unchecked(e);
        }
    }

    public static void deleteFile(File file) throws UncheckedIOException {
        deleteFile(file.toPath());
    }

    public static long size(Path path) throws UncheckedIOException {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw unchecked(e);
        }
    }

    public static Path getDirectory(Path path) {
        return Files.isDirectory(path) ? path : path.getParent();
    }

    public static File getDirectory(File file) {
        return file.isDirectory() ? file : file.getParentFile();
    }

    public static Path toPath(File file) {
        return file != null ? file.toPath() : null;
    }

    public static File toFile(Path path) {
        return path != null ? path.toFile() : null;
    }

    public static List<Path> listFileToPath(List<? extends File> files) {
        List<Path> result = new ArrayList<>(files.size());
        for (File file : files) {
            result.add(toPath(file));
        }
        return result;
    }

    public static List<File> listPathToFile(List<? extends Path> paths) {
        List<File> result = new ArrayList<>(paths.size());
        for (Path path : paths) {
            result.add(toFile(path));
        }
        return result;
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

    public static List<Path> listURLToPath(List<? extends URL> urls) throws IllegalArgumentException {
        List<Path> result = new ArrayList<>(urls.size());
        for (URL url : urls) {
            result.add(toPath(url));
        }
        return result;
    }

    public static List<File> listURLToFile(List<? extends URL> urls) throws IllegalArgumentException {
        List<File> result = new ArrayList<>(urls.size());
        for (URL url : urls) {
            result.add(toFile(url));
        }
        return result;
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

    public static List<URL> listPathToURL(List<? extends Path> paths) throws IllegalArgumentException {
        List<URL> result = new ArrayList<>(paths.size());
        for (Path path : paths) {
            result.add(toURL(path));
        }
        return result;
    }

    public static List<URL> listFileToURL(List<? extends File> files) throws IllegalArgumentException {
        List<URL> result = new ArrayList<>(files.size());
        for (File file : files) {
            result.add(toURL(file));
        }
        return result;
    }

    public static String toURLString(Path path) {
        return URLDecoder.decode(toURL(path).toExternalForm(), StandardCharsets.UTF_8);
    }

    public static String toURLString(File file) {
        return URLDecoder.decode(toURL(file).toExternalForm(), StandardCharsets.UTF_8);
    }

    private static UncheckedIOException unchecked(IOException e) {
        return new UncheckedIOException(e.getMessage(), e);
    }
}
