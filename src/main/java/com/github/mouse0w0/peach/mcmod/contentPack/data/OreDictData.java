package com.github.mouse0w0.peach.mcmod.contentPack.data;

import com.github.mouse0w0.peach.mcmod.Item;

import java.util.List;

public class OreDictData {

    private String id;
    private List<Item> entries;

    public OreDictData(String id, List<Item> entries) {
        this.id = id;
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public List<Item> getEntries() {
        return entries;
    }
}
