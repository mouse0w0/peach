package com.github.mouse0w0.peach.mcmod.element.impl;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.language.Localizable;

import java.util.Map;

public class ItemGroup extends Element implements Localizable {

    private String registerName;
    private String displayName;
    private Item icon = Item.AIR;

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Item getIcon() {
        return icon;
    }

    public void setIcon(Item icon) {
        this.icon = icon;
    }

    @Override
    public void getLocalizedText(String namespace, Map<String, String> localizedTexts) {
        localizedTexts.put("itemGroup." + namespace + "." + getRegisterName(), getDisplayName());
    }
}
