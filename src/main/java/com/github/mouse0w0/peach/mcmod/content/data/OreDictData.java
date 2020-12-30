package com.github.mouse0w0.peach.mcmod.content.data;

import com.github.mouse0w0.peach.mcmod.ItemRef;

import java.util.List;

public class OreDictData {

    private String id;
    private List<ItemRef> entries;

    public OreDictData(String id, List<ItemRef> entries) {
        this.id = id;
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public List<ItemRef> getEntries() {
        return entries;
    }
}
