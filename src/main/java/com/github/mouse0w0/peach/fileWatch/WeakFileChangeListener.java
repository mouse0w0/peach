package com.github.mouse0w0.peach.fileWatch;

import java.lang.ref.WeakReference;
import java.nio.file.Path;

public final class WeakFileChangeListener extends FileChangeListener {
    private final WeakReference<FileChangeListener> ref;

    public WeakFileChangeListener(FileChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        this.ref = new WeakReference<>(listener);
    }

    public boolean wasGarbageCollected() {
        return ref.get() == null;
    }

    public FileChangeListener getListener() {
        return ref.get();
    }

    @Override
    public void onFileCreate(ProjectFileWatcher watcher, Path path) {
        final FileChangeListener listener = ref.get();
        if (listener != null) {
            listener.onFileCreate(watcher, path);
        } else {
            watcher.removeListener(this);
        }
    }

    @Override
    public void onFileDelete(ProjectFileWatcher watcher, Path path) {
        final FileChangeListener listener = ref.get();
        if (listener != null) {
            listener.onFileDelete(watcher, path);
        } else {
            watcher.removeListener(this);
        }
    }

    @Override
    public void onFileModify(ProjectFileWatcher watcher, Path path) {
        final FileChangeListener listener = ref.get();
        if (listener != null) {
            listener.onFileModify(watcher, path);
        } else {
            watcher.removeListener(this);
        }
    }

    @Override
    public void onOverflow(ProjectFileWatcher watcher, Object context) {
        final FileChangeListener listener = ref.get();
        if (listener != null) {
            listener.onOverflow(watcher, context);
        } else {
            watcher.removeListener(this);
        }
    }
}
