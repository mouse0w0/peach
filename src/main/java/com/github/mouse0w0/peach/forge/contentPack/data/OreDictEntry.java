package com.github.mouse0w0.peach.forge.contentPack.data;

public class OreDictEntry {

    private String id;
    private int metadata;

    public OreDictEntry(String id, int metadata) {
        this.id = id;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public int getMetadata() {
        return metadata;
    }
}
