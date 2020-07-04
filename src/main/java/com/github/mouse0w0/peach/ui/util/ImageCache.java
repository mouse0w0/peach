package com.github.mouse0w0.peach.ui.util;

import com.github.mouse0w0.peach.util.FileUtils;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ImageCache {

    private static final Map<Key, Image> CACHE = new ConcurrentHashMap<>(16, 0.75f, 4);

    public static class Key {
        private final String url;
        private final double width;
        private final double height;

        private final boolean preserveRatio;
        private final boolean smooth;

        public Key(Path file, double width, double height) {
            this(FileUtils.toURLString(file), width, height, true, false);
        }

        public Key(Path file, double width, double height, boolean preserveRatio, boolean smooth) {
            this(FileUtils.toURLString(file), width, height, preserveRatio, smooth);
        }

        public Key(String url, double width, double height, boolean preserveRatio, boolean smooth) {
            this.url = url;
            this.width = width;
            this.height = height;

            this.preserveRatio = preserveRatio;
            this.smooth = smooth;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Double.compare(key.width, width) == 0 &&
                    Double.compare(key.height, height) == 0 &&
                    url.equals(key.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(url, width, height);
        }

        @Override
        public String toString() {
            return "Key{" +
                    "url='" + url + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    ", preserveRatio=" + preserveRatio +
                    ", smooth=" + smooth +
                    '}';
        }
    }

    public static Image getImage(Key key) {
        return getImage(key, true);
    }

    public static Image getImage(Key key, boolean backgroundLoading) {
        return CACHE.computeIfAbsent(key, $ -> createImage(key, backgroundLoading));
    }

    private static Image createImage(Key key, boolean backgroundLoading) {
        return new Image(key.url, key.width, key.height, key.preserveRatio, key.smooth, backgroundLoading);
    }
}
