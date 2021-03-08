package com.github.mouse0w0.peach.mcmod;

public class SoundType implements LocalizableEx {
    private String id;
    private String translationKey;
    private ItemRef icon;

    private transient String localizedText;

    public SoundType(String id, String translationKey, ItemRef icon) {
        this.id = id;
        this.translationKey = translationKey;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public ItemRef getIcon() {
        return icon;
    }

    @Override
    public String getLocalizedText() {
        return localizedText;
    }

    @Override
    public void setLocalizedText(String text) {
        this.localizedText = text;
    }
}
