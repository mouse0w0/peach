package com.github.mouse0w0.peach.forge.contentPack.data;

import com.github.mouse0w0.peach.forge.Item;

public class CreativeTabData {

    private String id;
    private String translationKey;
    private Item item;

    private transient String displayName;

    public CreativeTabData(String id, String translationKey, Item item) {
        this.id = id;
        this.translationKey = translationKey;
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public Item getItem() {
        return item;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
