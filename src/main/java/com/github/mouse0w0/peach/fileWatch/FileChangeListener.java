package com.github.mouse0w0.peach.fileWatch;

import java.nio.file.Path;

public abstract class FileChangeListener {
    public void onOverflow(ProjectFileWatcher watcher, Object context) {
    }

    public void onFileCreate(ProjectFileWatcher watcher, Path path) {
    }

    public void onFileDelete(ProjectFileWatcher watcher, Path path) {
    }

    public void onFileModify(ProjectFileWatcher watcher, Path path) {
    }
}
