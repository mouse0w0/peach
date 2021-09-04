package com.github.mouse0w0.peach.mcmod.util;

import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ResourceStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceStore.class);

    private final Path root;
    private final Path defaultStore;
    private final String extension;

    public ResourceStore(Path root, Path defaultStore) {
        this(root, defaultStore, null);
    }

    public ResourceStore(Path root, Path defaultStore, String extension) {
        this.root = root;
        this.defaultStore = defaultStore;
        this.extension = extension;
    }

    public String toRelative(Path file) {
        if (file == null) return null;
        String relative = root.relativize(file).toString();
        if (extension != null && relative.endsWith(extension)) {
            relative = relative.substring(0, relative.length() - extension.length());
        }
        return relative.replace('\\', '/');
    }

    public String toRelative(File file) {
        return toRelative(FileUtils.toPath(file));
    }

    public Path toAbsolutePath(String relative) {
        if (relative == null) return null;
        return extension == null ? root.resolve(relative) : root.resolve(relative + extension);
    }

    public File toAbsoluteFile(String relative) {
        if (relative == null) return null;
        return extension == null ? new File(root.toString(), relative) : new File(root.toString(), relative + extension);
    }

    public File store(File file) {
        return store(FileUtils.toPath(file)).toFile();
    }

    public Path store(Path file) {
        if (file == null) return null;
        if (file.startsWith(root)) return file;

        try {
            return ResourceUtils.copyToLowerCaseFile(file, defaultStore.resolve(file.getFileName()));
        } catch (IOException e) {
            LOGGER.error("Failed to copy file because an exception has occurred.", e);
            Alert.error("An exception has occurred!");
            return null;
        }
    }
}
