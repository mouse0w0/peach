package com.github.mouse0w0.peach.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class SilentFileUtils {
    public static void createDirectoriesIfNotExists(final Path path) throws UncheckedIOException {
        try {
            FileUtils.createDirectoriesIfNotExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void createFileIfNotExists(final Path path) throws UncheckedIOException {
        try {
            FileUtils.createFileIfNotExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void createParentIfNotExists(final Path path) throws UncheckedIOException {
        try {
            FileUtils.createParentIfNotExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Path forceCopy(final Path source, final Path target) throws UncheckedIOException {
        try {
            return FileUtils.forceCopy(source, target);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static long forceCopy(final InputStream in, final Path target) throws UncheckedIOException {
        try {
            return FileUtils.forceCopy(in, target);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static boolean isEmpty(final Path path) {
        try {
            return FileUtils.isEmpty(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static boolean notEmpty(final Path path) {
        try {
            return FileUtils.notEmpty(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
