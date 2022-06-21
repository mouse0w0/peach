package com.github.mouse0w0.peach.javafx.util;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ImageUtils {
    public static Image of(Path file) {
        return of(file, 0, 0, false, false);
    }

    public static Image of(Path file, int requestedWidth, int requestedHeight) {
        return of(file, requestedWidth, requestedHeight, false, false);
    }

    public static Image of(Path file, int requestedWidth, int requestedHeight, boolean preserveRatio, boolean smooth) {
        try (InputStream input = Files.newInputStream(file)) {
            return new Image(input, requestedWidth, requestedHeight, preserveRatio, smooth);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private ImageUtils() {
    }
}
