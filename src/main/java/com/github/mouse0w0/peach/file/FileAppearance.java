package com.github.mouse0w0.peach.file;

import com.github.mouse0w0.peach.extension.ExtensionPointName;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public interface FileAppearance {
    ExtensionPointName<FileAppearance> EXTENSION_POINT = ExtensionPointName.of("peach.fileAppearance");

    static void process(Project project, Path file, FileCell cell) {
        for (FileAppearance fileAppearance : FileAppearance.EXTENSION_POINT.getExtensions()) {
            if (fileAppearance.apply(project, file, cell)) {
                return;
            }
        }
    }

    /**
     * @param project the project.
     * @param file the file.
     * @param cell the cell of file.
     * @return if true, it means that the subsequent FileAppearance will be skipped.
     */
    boolean apply(Project project, Path file, FileCell cell);
}
