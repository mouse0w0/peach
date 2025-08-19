package com.github.mouse0w0.peach.fileWatch;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.project.Project;
import com.sun.nio.file.ExtendedWatchEventModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardWatchEventKinds.*;

public class ProjectFileWatcher implements Disposable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectFileWatcher.class);

    private static final WatchEvent.Kind<?>[] KINDS = {OVERFLOW, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};
    private static final WatchEvent.Modifier[] MODIFIERS = {ExtendedWatchEventModifier.FILE_TREE};

    private static final AtomicInteger NEXT_THREAD_ID = new AtomicInteger();

    private final Project project;
    private final FileChangeListener publisher;
    private final Thread thread;

    public static ProjectFileWatcher getInstance(Project project) {
        return project.getService(ProjectFileWatcher.class);
    }

    public ProjectFileWatcher(Project project) {
        this.project = project;
        this.publisher = project.getMessageBus().getPublisher(FileChangeListener.TOPIC);
        this.thread = new Thread(this::run, "File Watcher-" + NEXT_THREAD_ID.getAndIncrement());
        this.thread.start();
    }

    public Project getProject() {
        return project;
    }

    private void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            initialize(watchService);
            while (!thread.isInterrupted()) {
                fireEvents(watchService.take());
            }
        } catch (InterruptedException e) {
            thread.interrupt();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void initialize(WatchService watchService) throws IOException {
        project.getPath().register(watchService, KINDS, MODIFIERS);
    }

    private void fireEvents(WatchKey watchKey) {
        Path path = (Path) watchKey.watchable();
        FileChangeListener publisher = this.publisher;
        for (WatchEvent<?> event : watchKey.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();
            if (kind == ENTRY_CREATE) {
                publisher.onFileCreate(path.resolve((Path) event.context()));
            } else if (kind == ENTRY_DELETE) {
                publisher.onFileDelete(path.resolve((Path) event.context()));
            } else if (kind == ENTRY_MODIFY) {
                publisher.onFileModify(path.resolve((Path) event.context()));
            } else if (kind == OVERFLOW) {
                LOGGER.warn("File watcher overflow, context={}, project={}", event.context(), project.getName());
            }
        }
        watchKey.reset();
    }

    @Override
    public void dispose() {
        thread.interrupt();
    }
}
