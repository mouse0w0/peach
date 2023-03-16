package com.github.mouse0w0.peach.plugin;

import java.nio.file.Path;
import java.util.List;

public class PluginLoadException extends RuntimeException {
    private final List<Path> classpath;

    public PluginLoadException(String message, List<Path> classpath) {
        super(buildMessage(message, null, classpath));
        this.classpath = classpath;
    }

    public PluginLoadException(String message, Throwable cause, List<Path> classpath) {
        super(buildMessage(message, cause, classpath), cause);
        this.classpath = classpath;
    }

    public PluginLoadException(Throwable cause, List<Path> classpath) {
        super(buildMessage(null, cause, classpath), cause);
        this.classpath = classpath;
    }

    private static String buildMessage(String message, Throwable cause, List<Path> classpath) {
        return (message != null ? message : cause != null ? cause.getMessage() : null) + "\nClasspath: " + classpath;
    }

    public List<Path> getClasspath() {
        return classpath;
    }
}
