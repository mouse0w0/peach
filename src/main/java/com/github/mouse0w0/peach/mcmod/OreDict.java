package com.github.mouse0w0.peach.mcmod;

import java.util.List;

public class OreDict {

    private String id;
    private List<IdMetadata> entries;

    public OreDict(String id, List<IdMetadata> entries) {
        this.id = id;
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public List<IdMetadata> getEntries() {
        return entries;
    }
}
