package com.github.mouse0w0.peach.file;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.nio.file.Path;

public final class FileAppearances {

    public static void apply(Path path, StringProperty text, ObjectProperty<Image> icon) {
        for (FileAppearance fileAppearance : FileAppearance.EXTENSION_POINT.getExtensions()) {
            if (fileAppearance.accept(path)) {
                fileAppearance.apply(path, text, icon);
                return;
            }
        }
    }

    private FileAppearances() {
    }
}
