package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.extension.ExtensionPoint;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public interface FileEditorProvider {

    ExtensionPoint<FileEditorProvider> EXTENSION_POINT = ExtensionPoint.of("fileEditorProvider");

    boolean accept(Path file);

    FileEditor create(Project project, Path file);
}
