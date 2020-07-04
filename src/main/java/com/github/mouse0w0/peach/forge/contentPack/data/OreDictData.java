package com.github.mouse0w0.peach.forge.contentPack.data;

import com.github.mouse0w0.peach.forge.ItemToken;

import java.util.List;

public class OreDictData {

    private String id;
    private List<ItemToken> entries;

    public OreDictData(String id, List<ItemToken> entries) {
        this.id = id;
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public List<ItemToken> getEntries() {
        return entries;
    }
}
