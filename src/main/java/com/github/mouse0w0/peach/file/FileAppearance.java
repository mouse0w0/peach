package com.github.mouse0w0.peach.file;

import com.github.mouse0w0.peach.extension.ExtensionPoint;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.nio.file.Path;

public interface FileAppearance {
    ExtensionPoint<FileAppearance> EXTENSION_POINT = ExtensionPoint.of("fileAppearance");

    boolean isAcceptable(Path file);

    void apply(Path file, StringProperty text, ObjectProperty<Image> icon);
}
