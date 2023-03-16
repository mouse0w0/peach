package com.github.mouse0w0.peach.plugin;

public final class ExtensionPointDescriptor {
    private final String name;
    private final String className;
    private final boolean bean;

    public ExtensionPointDescriptor(String name, String className, boolean bean) {
        this.name = name;
        this.className = className;
        this.bean = bean;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public boolean isBean() {
        return bean;
    }
}
