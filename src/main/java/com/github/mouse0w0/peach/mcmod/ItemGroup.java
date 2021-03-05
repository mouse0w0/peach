package com.github.mouse0w0.peach.mcmod;

public class ItemGroup {

    private String id;
    private String translationKey;
    private ItemRef icon;

    private transient String displayName;

    public ItemGroup(String id, String translationKey, ItemRef icon) {
        this.id = id;
        this.translationKey = translationKey;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public ItemRef getIcon() {
        return icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
