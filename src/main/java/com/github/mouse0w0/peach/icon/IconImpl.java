package com.github.mouse0w0.peach.icon;

import com.github.mouse0w0.peach.util.FileUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

class IconImpl implements Icon {
    private static final Logger LOGGER = LoggerFactory.getLogger("Icon");

    private static final Image NO_INIT = new WritableImage(1, 1);

    private final String name;
    private final URL url;

    private Image image = NO_INIT;

    public IconImpl(@NotNull String name, @Nullable URL url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Image getImage() {
        if (image == NO_INIT) {
            image = createImage();
        }
        return image;
    }

    private Image createImage() {
        if (url == null) {
            return null;
        }
        try {
            String fileExtension = FileUtils.getFileExtension(url.getPath());
            if (fileExtension.equals("svg")) {
                return SwingFXUtils.toFXImage(SVGRasterizer.load(url, 0, 0), null);
            }
            return new Image(url.toExternalForm());
        } catch (Exception e) {
            LOGGER.warn("Cannot load icon, name={}, url={}", name, url, e);
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IconImpl icon = (IconImpl) o;

        return name.equals(icon.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Icon{" +
                "name='" + name + '\'' +
                ", url=" + url +
                '}';
    }
}
