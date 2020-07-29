package com.github.mouse0w0.peach.forge.contentPack.data;

import com.github.mouse0w0.peach.forge.Item;

public class CreativeTabData {

    private String id;
    private String translationKey;
    private Item item;

    public CreativeTabData(String id, String translationKey, Item item) {
        this.id = id;
        this.translationKey = translationKey;
        this.item = item;
    }
}
