package com.github.mouse0w0.peach.util;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassPathUtils {
    public static Path getPath(String name) {
        return getPath(name, Thread.currentThread().getContextClassLoader());
    }

    public static Path getPath(String name, ClassLoader classLoader) {
        URL resource = classLoader.getResource(name);
        if (resource == null) return null;

        try {
            return Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            throw new Error(e);
        }
    }
}
