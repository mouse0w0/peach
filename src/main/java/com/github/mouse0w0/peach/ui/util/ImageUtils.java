package com.github.mouse0w0.peach.ui.util;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ImageUtils {
    public static Image loadImage(Path file) {
        return loadImage(file, 0, 0, false, false);
    }

    public static Image loadImage(Path file, int requestedWidth, int requestedHeight) {
        return loadImage(file, requestedWidth, requestedHeight, false, false);
    }

    public static Image loadImage(Path file, int requestedWidth, int requestedHeight, boolean preserveRatio, boolean smooth) {
        try (InputStream input = Files.newInputStream(file)) {
            return new Image(input, requestedWidth, requestedHeight, preserveRatio, smooth);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private ImageUtils() {
    }
}
