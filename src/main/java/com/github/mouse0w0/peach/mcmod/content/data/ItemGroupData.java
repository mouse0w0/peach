package com.github.mouse0w0.peach.mcmod.content.data;

import com.github.mouse0w0.peach.mcmod.ItemRef;

public class ItemGroupData {

    private String id;
    private String translationKey;
    private ItemRef item;

    private transient String displayName;

    public ItemGroupData(String id, String translationKey, ItemRef item) {
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

    public ItemRef getItem() {
        return item;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
