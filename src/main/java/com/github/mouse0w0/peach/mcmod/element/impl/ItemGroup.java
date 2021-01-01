package com.github.mouse0w0.peach.mcmod.element.impl;

import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.language.Localizable;

import java.util.Map;

public class ItemGroup extends Element implements Localizable {

    private String identifier;
    private String displayName;
    private boolean hasSearchBar;
    private String background;
    private ItemRef icon = ItemRef.AIR;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isHasSearchBar() {
        return hasSearchBar;
    }

    public void setHasSearchBar(boolean hasSearchBar) {
        this.hasSearchBar = hasSearchBar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public ItemRef getIcon() {
        return icon;
    }

    public void setIcon(ItemRef icon) {
        this.icon = icon;
    }

    @Override
    public void getLocalizedText(String namespace, Map<String, String> localizedTexts) {
        localizedTexts.put("itemGroup." + namespace + "." + getIdentifier(), getDisplayName());
    }
}
