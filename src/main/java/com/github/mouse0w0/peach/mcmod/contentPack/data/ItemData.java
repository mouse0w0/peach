package com.github.mouse0w0.peach.mcmod.contentPack.data;

import java.nio.file.Path;

public class ItemData {

    private String id;
    private int metadata;
    private String translationKey;
    private boolean block;

    private transient String displayName;
    private transient Path displayImage;

    public ItemData(String id, int metadata, String translationKey, boolean block) {
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Path getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(Path displayImage) {
        this.displayImage = displayImage;
    }
}
