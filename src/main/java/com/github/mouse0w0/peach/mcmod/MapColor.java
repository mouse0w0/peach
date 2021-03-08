package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;

public class MapColor implements LocalizableEx {
    public static final MapColor INHERIT = new MapColor("inherit", "mapColor.inherit", ItemRef.AIR) {
        @Override
        public String getLocalizedText() {
            return I18n.translate(getTranslationKey());
        }

        @Override
        public void setLocalizedText(String text) {
            throw new UnsupportedOperationException();
        }
    };

    private String id;
    private String translationKey;
    private ItemRef icon;

    private transient String localizedText;

    public MapColor(String id, String translationKey, ItemRef icon) {
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

