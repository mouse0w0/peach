package com.github.mouse0w0.peach.ui.util;

import com.github.mouse0w0.peach.util.FileUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class CachedImage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedImage.class);

    private static final Cache<CachedImage, Image> CACHE = CacheBuilder.newBuilder().concurrencyLevel(4).build();

    private final String url;
    private final double width;
    private final double height;

    private final boolean preserveRatio;
    private final boolean smooth;

    public CachedImage(Path file) {
        this(FileUtils.toUrlAsString(file), 0, 0, true, false);
    }

    public CachedImage(Path file, double width, double height) {
        this(FileUtils.toUrlAsString(file), width, height, true, false);
    }

    public CachedImage(Path file, double width, double height, boolean preserveRatio, boolean smooth) {
        this(FileUtils.toUrlAsString(file), width, height, preserveRatio, smooth);
    }

    public CachedImage(String url, double width, double height) {
        this(url, width, height, true, false);
    }

    public CachedImage(String url, double width, double height, boolean preserveRatio, boolean smooth) {
        this.url = url;
        this.width = width;
        this.height = height;

        this.preserveRatio = preserveRatio;
        this.smooth = smooth;
    }

    public Image getImage() {
        return getImage(true);
    }

    public Image getImage(boolean backgroundLoading) {
        try {
            return CACHE.get(this, () -> newImage(backgroundLoading));
        } catch (ExecutionException e) {
            LOGGER.error("Failed to get image " + url + " from cache.", e);
            return newImage(backgroundLoading);
        }
    }

    private Image newImage(boolean backgroundLoading) {
        return new Image(url, width, height, preserveRatio, smooth, backgroundLoading);
    }

    public void invalidate() {
        CACHE.invalidate(this);
    }

    public String getUrl() {
        return url;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isPreserveRatio() {
        return preserveRatio;
    }

    public boolean isSmooth() {
        return smooth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CachedImage that = (CachedImage) o;
        return Double.compare(that.width, width) == 0 &&
                Double.compare(that.height, height) == 0 &&
                preserveRatio == that.preserveRatio &&
                smooth == that.smooth &&
                url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, width, height, preserveRatio, smooth);
    }

    @Override
    public String toString() {
        return "CachedImage{" +
                "url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", preserveRatio=" + preserveRatio +
                ", smooth=" + smooth +
                '}';
    }
}
