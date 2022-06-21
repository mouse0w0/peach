package com.github.mouse0w0.peach.mcmod;

import javafx.scene.image.Image;

public class Item {

    private String id;
    private int metadata;
    private String translationKey;
    private boolean block;

    private transient String localizedText;
    private transient Image image;

    public Item(String id, int metadata, String translationKey, boolean block) {
        this.id = id;
        this.metadata = metadata;
        this.translationKey = translationKey;
        this.block = block;
    }

    public String getId() {
        return id;
    }

    public String getNamespace() {
        return id.substring(0, id.indexOf(':'));
    }

    public String getName() {
        return id.substring(id.indexOf(':') + 1);
    }

    public int getMetadata() {
        return metadata;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public boolean isBlock() {
        return block;
    }

    public String getLocalizedText() {
        return localizedText;
    }

    public void setLocalizedText(String text) {
        this.localizedText = text;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
