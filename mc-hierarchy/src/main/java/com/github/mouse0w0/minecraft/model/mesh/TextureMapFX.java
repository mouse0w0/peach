package com.github.mouse0w0.minecraft.model.mesh;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TextureMapFX implements TextureMap {
    private final Image image;
    private final Map<String, Vector4f> texCoords;

    public static Builder builder() {
        return new Builder();
    }

    private TextureMapFX(Image image, Map<String, Vector4f> texCoords) {
        this.image = image;
        this.texCoords = texCoords;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public Vector4f getTexCoord(String name) {
        return texCoords.get(name);
    }

    public static class Builder {
        private Map<String, Image> textureMap = new LinkedHashMap<>();

        public Builder texture(String name, Image texture) {
            textureMap.put(name, texture);
            return this;
        }

        public TextureMapFX build() {
            float mapWidth = 0, mapHeight = 0;
            for (Image image : textureMap.values()) {
                mapWidth += image.getWidth();
                if (image.getHeight() > mapHeight) mapHeight = (float) image.getHeight();
            }

            WritableImage texMap = new WritableImage((int) mapWidth, (int) mapHeight);
            Map<String, Vector4f> texCoords = new HashMap<>();
            PixelWriter pixelWriter = texMap.getPixelWriter();

            int width = 0, height = 0;
            for (Map.Entry<String, Image> entry : textureMap.entrySet()) {
                Image image = entry.getValue();
                int imageWidth = (int) image.getWidth(), imageHeight = (int) image.getHeight();
                Vector4f uv = new Vector4f(width / mapWidth, height / mapHeight,
                        (width + imageWidth) / mapWidth, (height + imageHeight) / mapHeight);
                texCoords.put(entry.getKey(), uv);
                pixelWriter.setPixels(width, height, imageWidth, imageHeight, image.getPixelReader(), 0, 0);
                width += imageWidth;
                height += imageHeight;
            }

            return new TextureMapFX(texMap, texCoords);
        }
    }
}
