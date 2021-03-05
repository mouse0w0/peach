package com.github.mouse0w0.peach.mcmod;

public interface LocalizableEx extends Localizable {
    String getTranslationKey();

    ItemRef getIcon();

    void setLocalizedText(String text);
}
