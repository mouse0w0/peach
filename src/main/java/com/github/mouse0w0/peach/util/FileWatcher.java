package com.github.mouse0w0.peach.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.nio.file.*;
import java.util.function.Consumer;

public class FileWatcher {

    private final Path path;
    private Multimap<WatchEvent.Kind<?>, Consumer<Path>> listeners = HashMultimap.create();
    private boolean receiveRepeatedEvent;
    private WatchEvent.Modifier[] modifiers;

    private Thread thread;

    public FileWatcher(Path path, WatchEvent.Modifier... modifiers) {
        this(path, false, modifiers);
    }

    public FileWatcher(Path path, boolean receiveRepeatedEvent, WatchEvent.Modifier... modifiers) {
        this.path = path;
        this.receiveRepeatedEvent = receiveRepeatedEvent;
        this.modifiers = modifiers;
    }

    public Path getPath() {
        return path;
    }

    public void addListener(WatchEvent.Kind<?> event, Consumer<Path> listener) {
        listeners.put(event, listener);
    }

    public void removeListener(Consumer<Path> listener) {
        listeners.removeAll(listener);
    }

    public boolean isReceiveRepeatedEvent() {
        return receiveRepeatedEvent;
    }

    public void setReceiveRepeatedEvent(boolean receiveRepeatedEvent) {
        this.receiveRepeatedEvent = receiveRepeatedEvent;
    }

    public WatchEvent.Modifier[] getModifiers() {
        return modifiers;
    }

    public void setModifiers(WatchEvent.Modifier[] modifiers) {
        this.modifiers = modifiers;
    }

    public void start() {
        thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    private void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            WatchKey key = path.register(watchService,
                    listeners.keySet().toArray(new WatchEvent.Kind[0]),
                    modifiers);
            while (!Thread.interrupted()) {
                watchService.take();
                key.pollEvents().forEach(watchEvent -> {
                    if (receiveRepeatedEvent || watchEvent.count() == 1) {
                        Path absolutePath = path.resolve((Path) watchEvent.context());
                        listeners.get(watchEvent.kind()).forEach(consumer -> consumer.accept(absolutePath));
                    }
                });
                key.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ignored) {
        }
    }
}
