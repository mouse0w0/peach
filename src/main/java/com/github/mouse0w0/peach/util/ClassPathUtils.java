package com.github.mouse0w0.peach.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class ClassPathUtils {
    public static Path getPath(String name) throws IOException, ResourceNotFoundException {
        return getPath(name, Thread.currentThread().getContextClassLoader());
    }

    public static Path getPath(String name, ClassLoader classLoader) throws IOException, ResourceNotFoundException {
        URL resource = getResource(name, classLoader);
        try {
            URI uri = resource.toURI();
            if ("jar".equals(uri.getScheme())) {
                try {
                    FileSystems.getFileSystem(uri);
                } catch (FileSystemNotFoundException e) {
                    FileSystems.newFileSystem(uri, Collections.emptyMap());
                }
            }
            return Paths.get(uri);
        } catch (URISyntaxException e) {
            throw new Error(e);
        }
    }

    public static URL getResource(String name) throws ResourceNotFoundException {
        return getResource(name, Thread.currentThread().getContextClassLoader());
    }

    public static URL getResource(String name, ClassLoader classLoader) throws ResourceNotFoundException {
        URL resource = classLoader.getResource(name);
        if (resource == null) throw new ResourceNotFoundException(name);
        return resource;
    }

    public static InputStream getResourceAsStream(String name) throws ResourceNotFoundException {
        return getResourceAsStream(name, Thread.currentThread().getContextClassLoader());
    }

    public static InputStream getResourceAsStream(String name, ClassLoader classLoader) throws ResourceNotFoundException {
        InputStream stream = classLoader.getResourceAsStream(name);
        if (stream == null) throw new ResourceNotFoundException(name);
        return stream;
    }
}
