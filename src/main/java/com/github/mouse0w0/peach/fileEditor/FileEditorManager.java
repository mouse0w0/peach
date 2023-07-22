package com.github.mouse0w0.peach.fileEditor;

import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public interface FileEditorManager {

    static FileEditorManager getInstance(Project project) {
        return project.getService(FileEditorManager.class);
    }

    FileEditor getFileEditor(Path file);

    boolean isFileOpened(Path file);

    void open(Path file);

    boolean close(FileEditor fileEditor);

    boolean close(Path file);
}
