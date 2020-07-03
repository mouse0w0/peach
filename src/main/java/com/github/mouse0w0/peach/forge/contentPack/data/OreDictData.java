package com.github.mouse0w0.peach.forge.contentPack.data;

import java.util.List;

public class OreDictData {

    private String id;
    private List<OreDictEntry> entries;

    public OreDictData(String id, List<OreDictEntry> entries) {
        this.id = id;
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public List<OreDictEntry> getEntries() {
        return entries;
    }
}
