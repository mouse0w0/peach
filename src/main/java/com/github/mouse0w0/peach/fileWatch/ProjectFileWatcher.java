package com.github.mouse0w0.peach.fileWatch;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.ArrayUtils;
import com.sun.nio.file.ExtendedWatchEventModifier;
import com.sun.nio.file.SensitivityWatchEventModifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardWatchEventKinds.*;

public class ProjectFileWatcher implements Disposable {
    private static final WatchEvent.Kind<?>[] KINDS = {OVERFLOW, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};
    private static final WatchEvent.Modifier[] MODIFIERS = {SensitivityWatchEventModifier.HIGH, ExtendedWatchEventModifier.FILE_TREE};

    private static final AtomicInteger NEXT_ID = new AtomicInteger();

    private final Project project;
    private final Path path;
    private final Thread thread;

    private FileChangeListener[] listeners;

    public static ProjectFileWatcher getInstance(Project project) {
        return project.getService(ProjectFileWatcher.class);
    }

    public ProjectFileWatcher(Project project) {
        this.project = project;
        this.path = project.getPath();
        this.thread = new Thread(this::run, "File Watcher-" + NEXT_ID.getAndIncrement());
        this.thread.start();
    }

    public Project getProject() {
        return project;
    }

    public Path getPath() {
        return path;
    }

    public synchronized void addListener(FileChangeListener listener) {
        if (listeners == null) {
            listeners = new FileChangeListener[]{listener};
        } else {
            listeners = ArrayUtils.add(listeners, listener);
        }
    }

    public synchronized void removeListener(FileChangeListener listener) {
        if (listeners != null) {
            int index = ArrayUtils.indexOf(listeners, listener);
            if (index != ArrayUtils.INDEX_NOT_FOUND) {
                listeners = ArrayUtils.remove(listeners, index);
            }
        }
    }

    private void run() {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            WatchKey watchKey = path.register(watcher, KINDS, MODIFIERS);
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
        if (listeners == null) return;
        for (WatchEvent<?> event : events) {
            WatchEvent.Kind<?> kind = event.kind();
            if (kind == OVERFLOW) {
                Object context = event.context();
                for (FileChangeListener listener : listeners) {
                    listener.onOverflow(this, context);
                }
            } else if (kind == ENTRY_CREATE) {
                Path resolved = path.resolve((Path) event.context());
                for (FileChangeListener listener : listeners) {
                    listener.onFileCreate(this, resolved);
                }
            } else if (kind == ENTRY_DELETE) {
                Path resolved = path.resolve((Path) event.context());
                for (FileChangeListener listener : listeners) {
                    listener.onFileDelete(this, resolved);
                }
            } else if (kind == ENTRY_MODIFY) {
                Path resolved = path.resolve((Path) event.context());
                for (FileChangeListener listener : listeners) {
                    listener.onFileModify(this, resolved);
                }
            } else {
                throw new Error("Cannot reachable");
            }
        }
    }

    @Override
    public void dispose() {
        thread.interrupt();
    }
}
