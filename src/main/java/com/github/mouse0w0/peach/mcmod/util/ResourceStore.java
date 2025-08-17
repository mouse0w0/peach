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
    private final String suffix;

    public ResourceStore(Path basePath) {
        this(basePath, basePath, null);
    }

    public ResourceStore(Path basePath, Path defaultPath) {
        this(basePath, defaultPath, null);
    }

    public ResourceStore(Path basePath, Path defaultPath, String suffix) {
        this.basePath = basePath;
        this.defaultPath = defaultPath;
        this.suffix = suffix;
    }

    public Path getBasePath() {
        return basePath;
    }

    public Path getDefaultPath() {
        return defaultPath;
    }

    public String getSuffix() {
        return suffix;
    }

    public String relativize(Path file) {
        if (file == null) return null;
        if (!file.startsWith(basePath)) return file.toString();
        String relative = basePath.relativize(file).toString();
        if (suffix != null && relative.endsWith(suffix)) {
            relative = relative.substring(0, relative.length() - suffix.length());
        }
        return relative.replace('\\', '/');
    }

    public String relativize(File file) {
        return relativize(FileUtils.toPath(file));
    }

    public Path resolve(String relative) {
        if (relative == null) return null;
        if (relative.isEmpty()) return basePath;
        Path file = Paths.get(relative);
        if (file.isAbsolute()) return file;
        return suffix == null ? basePath.resolve(relative) : basePath.resolve(relative + suffix);
    }

    public File resolveToFile(String relative) {
        return FileUtils.toFile(resolve(relative));
    }

    public Path store(Path file) {
        if (file == null) return null;
        if (file.startsWith(basePath)) return file;

        try {
            return ResourceUtils.copyToLowerCaseFile(file, defaultPath.resolve(file.getFileName()));
        } catch (IOException e) {
            LOGGER.error("Failed to copy file because an exception has occurred.", e);
            Alert.error("An exception has occurred!"); // TODO: open exception dialog
            return null;
        }
    }

    public File store(File file) {
        return FileUtils.toFile(store(FileUtils.toPath(file)));
    }
}
