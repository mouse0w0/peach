package com.github.mouse0w0.peach.file;

import java.nio.file.Path;

public final class FileAppearances {

    public static void apply(Path file, FileCell cell) {
        for (FileAppearance fileAppearance : FileAppearance.EXTENSION_POINT.getExtensions()) {
            if (fileAppearance.apply(file, cell)) {
                return;
            }
        }
    }

    private FileAppearances() {
    }
}
