package com.github.mouse0w0.peach.mcmod;

import java.util.List;

public class OreDict {

    private String id;
    private List<ItemRef> entries;

    public OreDict(String id, List<ItemRef> entries) {
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
