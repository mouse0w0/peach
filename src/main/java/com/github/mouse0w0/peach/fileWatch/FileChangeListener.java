package com.github.mouse0w0.peach.fileWatch;

import java.nio.file.Path;

public interface FileChangeListener {
    default void onOverflow(ProjectFileWatcher watcher, Object context) {
    }

    default void onFileCreate(ProjectFileWatcher watcher, Path path) {
    }

    default void onFileDelete(ProjectFileWatcher watcher, Path path) {
    }

    default void onFileModify(ProjectFileWatcher watcher, Path path) {
    }
}
