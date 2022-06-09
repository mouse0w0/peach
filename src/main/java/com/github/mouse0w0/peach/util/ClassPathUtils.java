package com.github.mouse0w0.peach.util;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassPathUtils {
    public static Path getPath(String name) throws FileNotFoundException {
        return getPath(name, Thread.currentThread().getContextClassLoader());
    }

    public static Path getPath(String name, ClassLoader classLoader) throws FileNotFoundException {
        URL resource = classLoader.getResource(name);
        if (resource == null) throw new FileNotFoundException(name);

        try {
            return Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            throw new Error(e);
        }
    }
}
