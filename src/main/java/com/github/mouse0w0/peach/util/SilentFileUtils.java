package com.github.mouse0w0.peach.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class SilentFileUtils {
    public static void createDirectoriesIfNotExists(Path path) throws UncheckedIOException {
        try {
            FileUtils.createDirectoriesIfNotExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void createFileIfNotExists(Path path) throws UncheckedIOException {
        try {
            FileUtils.createFileIfNotExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void createParentIfNotExists(Path path) throws UncheckedIOException {
        try {
            FileUtils.createParentIfNotExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Path forceCopy(Path source, Path target) throws UncheckedIOException {
        try {
            return FileUtils.forceCopy(source, target);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static long forceCopy(InputStream in, Path target) throws UncheckedIOException {
        try {
            return FileUtils.forceCopy(in, target);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static boolean isEmpty(Path path) throws UncheckedIOException {
        try {
            return FileUtils.isEmpty(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static boolean notEmpty(Path path) throws UncheckedIOException {
        try {
            return FileUtils.notEmpty(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
