package com.github.mouse0w0.peach.mcmod;

public class IconicData {
    private final String id;
    private final String name;
    private final IdMetadata icon;

    public IconicData(String id, String name, IdMetadata icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public IdMetadata getIcon() {
        return icon;
    }
}
