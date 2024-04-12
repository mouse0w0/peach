package com.github.mouse0w0.peach.mcmod;

import javafx.scene.image.Image;

public class ItemData {
    private final Identifier id;
    private final int metadata;
    private final int maxStackSize;
    private final int maxDamage;
    private final boolean isBlock;
    private final String name;
    private final Image texture;

    public ItemData(Identifier id, int metadata, int maxStackSize, int maxDamage, boolean isBlock, String name, Image texture) {
        this.id = id;
        this.metadata = metadata;
        this.maxStackSize = maxStackSize;
        this.maxDamage = maxDamage;
        this.isBlock = isBlock;
        this.name = name;
        this.texture = texture;
    }

    public Identifier getId() {
        return id;
    }

    public int getMetadata() {
        return metadata;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public String getName() {
        return name;
    }

    public Image getTexture() {
        return texture;
    }
}
