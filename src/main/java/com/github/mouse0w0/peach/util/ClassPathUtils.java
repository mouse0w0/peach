package com.github.mouse0w0.peach.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class ClassPathUtils {
    public static Path getPath(String name) throws IOException {
        return getPath(name, Thread.currentThread().getContextClassLoader());
    }

    public static Path getPath(String name, ClassLoader classLoader) throws IOException {
        URL resource = classLoader.getResource(name);
        if (resource == null) throw new FileNotFoundException(name);
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
}
