package com.github.mouse0w0.peach.mcmod.util;

import com.github.mouse0w0.peach.ui.dialog.Alert;
import com.github.mouse0w0.peach.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceStore.class);

    private final Path basePath;
    private final Path defaultPath;
    private final String hiddenSuffix;

    public ResourceStore(Path basePath, Path defaultPath) {
        this(basePath, defaultPath, null);
    }

    public ResourceStore(Path basePath, Path defaultPath, String hiddenSuffix) {
        this.basePath = basePath;
        this.defaultPath = defaultPath;
        this.hiddenSuffix = hiddenSuffix;
    }

    public Path getBasePath() {
        return basePath;
    }

    public Path getDefaultPath() {
        return defaultPath;
    }

    public String getHiddenSuffix() {
        return hiddenSuffix;
    }

    public String toRelative(Path file) {
        if (file == null) return null;
        if (!file.startsWith(basePath)) return file.toString();
        String relative = basePath.relativize(file).toString();
        if (hiddenSuffix != null && relative.endsWith(hiddenSuffix)) {
            relative = relative.substring(0, relative.length() - hiddenSuffix.length());
        }
        return relative.replace('\\', '/');
    }

    public String toRelative(File file) {
        return toRelative(FileUtils.toPath(file));
    }

    public Path toAbsolutePath(String relative) {
        if (relative == null) return null;
        if (relative.isEmpty()) return basePath;
        Path file = Paths.get(relative);
        if (file.isAbsolute()) return file;
        return hiddenSuffix == null ? basePath.resolve(relative) : basePath.resolve(relative + hiddenSuffix);
    }

    public File toAbsoluteFile(String relative) {
        return FileUtils.toFile(toAbsolutePath(relative));
    }

    public Path store(Path file) {
        if (file == null) return null;
        if (file.startsWith(basePath)) return file;

        try {
            return ResourceUtils.copyToLowerCaseFile(file, defaultPath.resolve(file.getFileName()));
        } catch (IOException e) {
            LOGGER.error("Failed to copy file because an exception has occurred.", e);
            Alert.error("An exception has occurred!");
            return null;
        }
    }

    public File store(File file) {
        return FileUtils.toFile(store(FileUtils.toPath(file)));
    }
}
