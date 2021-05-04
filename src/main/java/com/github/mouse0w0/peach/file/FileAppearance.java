package com.github.mouse0w0.peach.file;

import com.github.mouse0w0.peach.extension.ExtensionPoint;

import java.nio.file.Path;

public interface FileAppearance {
    ExtensionPoint<FileAppearance> EXTENSION_POINT = ExtensionPoint.of("fileAppearance");

    /**
     * @param file the file.
     * @param cell the cell of file.
     * @return if true, it means that the subsequent FileAppearance will be skipped.
     */
    boolean apply(Path file, FileCell cell);
}
