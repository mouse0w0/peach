package com.github.mouse0w0.peach.plugin;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PluginClassLoader extends URLClassLoader {
    static {
        registerAsParallelCapable();
    }

    private final Plugin plugin;
    private final ClassLoader[] parents;

    public PluginClassLoader(URL[] urls, Plugin plugin, ClassLoader[] parents) {
        super(urls, null);
        this.plugin = plugin;
        this.parents = parents;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return loadClassFromSelf(name);
        } catch (ClassNotFoundException ignored) {
        }
        for (ClassLoader parent : parents) {
            try {
                if (parent instanceof PluginClassLoader) {
                    return ((PluginClassLoader) parent).loadClassFromSelf(name);
                } else {
                    return parent.loadClass(name);
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        throw new ClassNotFoundException(name);
    }

    Class<?> loadClassFromSelf(String name) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                c = findClass(name);
            }
            return c;
        }
    }

    @Override
    public URL getResource(String name) {
        Objects.requireNonNull(name);
        URL url = findResource(name);
        if (url != null) {
            return url;
        }
        for (ClassLoader parent : parents) {
            if (parent instanceof PluginClassLoader) {
                url = ((PluginClassLoader) parent).findResource(name);
            } else {
                url = parent.getResource(name);
            }
            if (url != null) {
                return url;
            }
        }
        return null;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Objects.requireNonNull(name);
        int parentCount = parents.length;
        Enumeration<URL>[] enums = new Enumeration[parentCount + 1];
        enums[0] = findResources(name);
        for (int i = 0; i < parentCount; i++) {
            ClassLoader parent = parents[i];
            if (parent instanceof PluginClassLoader) {
                enums[i + 1] = ((PluginClassLoader) parent).findResources(name);
            } else {
                enums[i + 1] = parent.getResources(name);
            }
        }
        return new CompoundEnumeration<>(enums);
    }

    private static final class CompoundEnumeration<E> implements Enumeration<E> {
        private final Enumeration<E>[] enums;
        private int index;

        public CompoundEnumeration(Enumeration<E>[] enums) {
            this.enums = enums;
        }

        private boolean next() {
            while (index < enums.length) {
                if (enums[index] != null && enums[index].hasMoreElements()) {
                    return true;
                }
                index++;
            }
            return false;
        }

        public boolean hasMoreElements() {
            return next();
        }

        public E nextElement() {
            if (!next()) {
                throw new NoSuchElementException();
            }
            return enums[index].nextElement();
        }
    }
}
