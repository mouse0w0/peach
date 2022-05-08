package com.github.mouse0w0.peach.util;

import com.sun.nio.file.ExtendedWatchEventModifier;
import com.sun.nio.file.SensitivityWatchEventModifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class FileWatcher implements Runnable {

    private static volatile FileWatcher instance;

    public static FileWatcher getDefault() {
        if (instance == null) {
            synchronized (FileWatcher.class) {
                if (instance == null) {
                    instance = new FileWatcher();
                    instance.start();
                }
            }
        }
        return instance;
    }

    private static final AtomicInteger NEXT_ID = new AtomicInteger();

    private final WatchService watchService;
    private final Map<WatchKey, Watch> watchMap = new ConcurrentHashMap<>();

    private Thread thread;

    public FileWatcher() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Configurer configurer() {
        return new Configurer(this);
    }

    public void start() {
        thread = new Thread(this, "File Watcher-" + NEXT_ID.getAndIncrement());
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    @Override
    public void run() {
        try {
            while (true) {
                watchMap.get(watchService.take()).pollEvents();
            }
        } catch (InterruptedException ignored) {
        } finally {
            if (watchService != null) {
                try {
                    watchService.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static final class Configurer {
        private static final WatchEvent.Kind<?>[] EMPTY_KIND_ARRAY = new WatchEvent.Kind[0];
        private static final WatchEvent.Modifier[] EMPTY_MODIFIER_ARRAY = new WatchEvent.Modifier[0];

        private final FileWatcher watcher;
        private Path path;
        private WatchEvent.Kind<?>[] kinds = EMPTY_KIND_ARRAY;
        private WatchEvent.Modifier[] modifiers = EMPTY_MODIFIER_ARRAY;
        private boolean acceptRepeatedEvent;
        private Consumer<Path> createConsumer;
        private Consumer<Path> deleteConsumer;
        private Consumer<Path> modifyConsumer;

        private Configurer(FileWatcher watcher) {
            this.watcher = watcher;
        }

        public Configurer path(Path path) {
            this.path = path;
            return this;
        }

        public Configurer watchCreate(Consumer<Path> consumer) {
            this.kinds = ArrayUtils.add(kinds, StandardWatchEventKinds.ENTRY_CREATE);
            this.createConsumer = consumer;
            return this;
        }

        public Configurer watchDelete(Consumer<Path> consumer) {
            this.kinds = ArrayUtils.add(kinds, StandardWatchEventKinds.ENTRY_DELETE);
            this.deleteConsumer = consumer;
            return this;
        }

        public Configurer watchModify(Consumer<Path> consumer) {
            this.kinds = ArrayUtils.add(kinds, StandardWatchEventKinds.ENTRY_MODIFY);
            this.modifyConsumer = consumer;
            return this;
        }


        public Configurer low() {
            this.modifiers = ArrayUtils.add(modifiers, SensitivityWatchEventModifier.LOW);
            return this;
        }

        public Configurer medium() {
            this.modifiers = ArrayUtils.add(modifiers, SensitivityWatchEventModifier.MEDIUM);
            return this;
        }

        public Configurer high() {
            this.modifiers = ArrayUtils.add(modifiers, SensitivityWatchEventModifier.HIGH);
            return this;
        }

        public Configurer fileTree() {
            this.modifiers = ArrayUtils.add(modifiers, ExtendedWatchEventModifier.FILE_TREE);
            return this;
        }

        public Configurer acceptRepeatedEvent() {
            acceptRepeatedEvent = true;
            return this;
        }

        public Watch watch() {
            try {
                WatchKey watchKey = path.register(watcher.watchService, kinds, modifiers);
                Watch watch = new Watch(watcher, watchKey, this);
                watcher.watchMap.put(watchKey, watch);
                return watch;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    public static final class Watch {
        private final FileWatcher watcher;
        private final WatchKey watchKey;
        private final Path path;
        private final WatchEvent.Kind<?>[] kinds;
        private final WatchEvent.Modifier[] modifiers;
        private final boolean acceptRepeatedEvent;
        private final Consumer<Path> createConsumer;
        private final Consumer<Path> deleteConsumer;
        private final Consumer<Path> modifyConsumer;

        private Watch(FileWatcher watcher, WatchKey watchKey, Configurer configurer) {
            this.watcher = watcher;
            this.watchKey = watchKey;
            this.path = configurer.path;
            this.kinds = configurer.kinds;
            this.modifiers = configurer.modifiers;
            this.acceptRepeatedEvent = configurer.acceptRepeatedEvent;
            this.createConsumer = configurer.createConsumer;
            this.deleteConsumer = configurer.deleteConsumer;
            this.modifyConsumer = configurer.modifyConsumer;
        }

        public Path getPath() {
            return path;
        }

        public WatchEvent.Kind<?>[] getKinds() {
            return kinds;
        }

        public WatchEvent.Modifier[] getModifiers() {
            return modifiers;
        }

        public boolean isAcceptRepeatedEvent() {
            return acceptRepeatedEvent;
        }

        public boolean isValid() {
            return watchKey.isValid();
        }

        public void pollEvents() {
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (acceptRepeatedEvent || event.count() == 1) {
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        createConsumer.accept(path.resolve((Path) event.context()));
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        deleteConsumer.accept(path.resolve((Path) event.context()));
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        modifyConsumer.accept(path.resolve((Path) event.context()));
                    } else {
                        throw new Error("Unreachable");
                    }
                }
            }
            watchKey.reset();
        }

        public void cancel() {
            watchKey.cancel();
            watcher.watchMap.remove(watchKey);
        }
    }
}
