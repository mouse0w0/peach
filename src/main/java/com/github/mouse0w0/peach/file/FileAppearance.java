package com.github.mouse0w0.peach.file;

import com.github.mouse0w0.peach.extension.ExtensionPoint;

import java.nio.file.Path;

public interface FileAppearance {
    ExtensionPoint<FileAppearance> EXTENSION_POINT = ExtensionPoint.of("fileAppearance");

    boolean accept(Path file);

    void apply(Path file, FileCell cell);
}
