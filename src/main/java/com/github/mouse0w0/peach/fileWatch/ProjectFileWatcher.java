package com.github.mouse0w0.peach.fileWatch;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.Disposable;
import com.sun.nio.file.ExtendedWatchEventModifier;
import com.sun.nio.file.SensitivityWatchEventModifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardWatchEventKinds.*;

public class ProjectFileWatcher implements Disposable {
    private static final WatchEvent.Kind<?>[] KINDS = {OVERFLOW, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};
    private static final WatchEvent.Modifier[] MODIFIERS = {SensitivityWatchEventModifier.HIGH, ExtendedWatchEventModifier.FILE_TREE};

    private static final AtomicInteger NEXT_ID = new AtomicInteger();

    private final Project project;
    private final Path projectPath;
    private final List<FileChangeListener> listeners;
    private final Thread thread;

    public static ProjectFileWatcher getInstance(Project project) {
        return project.getService(ProjectFileWatcher.class);
    }

    public ProjectFileWatcher(Project project) {
        this.project = project;
        this.projectPath = project.getPath();
        this.listeners = new ArrayList<>();
        this.thread = new Thread(this::run, "File Watcher-" + NEXT_ID.getAndIncrement());
        this.thread.start();
    }

    public Project getProject() {
        return project;
    }

    public Path getProjectPath() {
        return projectPath;
    }

    public void addListener(FileChangeListener listener) {
        synchronized (this) {
            listeners.add(listener);
        }
    }

    public void removeListener(FileChangeListener listener) {
        synchronized (this) {
            listeners.remove(listener);
        }
    }

    private void run() {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            final WatchKey watchKey = projectPath.register(watcher, KINDS, MODIFIERS);
            while (true) {
                watcher.take();
                fireEvents(watchKey.pollEvents());
                watchKey.reset();
            }
        } catch (InterruptedException ignored) {
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void fireEvents(List<WatchEvent<?>> events) {
        synchronized (this) {
            for (WatchEvent<?> event : events) {
                fireEvent(event);
            }
        }
    }

    private void fireEvent(WatchEvent<?> event) {
        if (event.count() != 1) return;

        final WatchEvent.Kind<?> kind = event.kind();
        if (kind == OVERFLOW) {
            final Object context = event.context();
            for (FileChangeListener listener : listeners) {
                listener.onOverflow(this, context);
            }
        } else if (kind == ENTRY_CREATE) {
            final Path path = projectPath.resolve((Path) event.context());
            for (FileChangeListener listener : listeners) {
                listener.onFileCreate(this, path);
            }
        } else if (kind == ENTRY_DELETE) {
            final Path path = projectPath.resolve((Path) event.context());
            for (FileChangeListener listener : listeners) {
                listener.onFileDelete(this, path);
            }
        } else if (kind == ENTRY_MODIFY) {
            final Path path = projectPath.resolve((Path) event.context());
            for (FileChangeListener listener : listeners) {
                listener.onFileModify(this, path);
            }
        } else {
            throw new Error("Cannot reachable");
        }
    }

    @Override
    public void dispose() {
        thread.interrupt();
    }
}
