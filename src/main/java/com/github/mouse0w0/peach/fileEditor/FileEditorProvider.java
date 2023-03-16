package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.extension.ExtensionPointName;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public interface FileEditorProvider {

    ExtensionPointName<FileEditorProvider> EXTENSION_POINT = ExtensionPointName.of("peach.fileEditorProvider");

    boolean accept(Path file);

    FileEditor create(Project project, Path file);
}
