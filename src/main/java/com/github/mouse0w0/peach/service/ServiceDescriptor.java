package com.github.mouse0w0.peach.service;

public final class ServiceDescriptor {
    private final String interfaceClassName;
    private final String implementationClassName;
    private final boolean override;
    private final PreloadMode preload;

    public enum PreloadMode {
        TRUE, AWAIT, FALSE;
    }

    public ServiceDescriptor(String interfaceClassName, String implementationClassName, boolean override, PreloadMode preload) {
        this.interfaceClassName = interfaceClassName;
        this.implementationClassName = implementationClassName;
        this.override = override;
        this.preload = preload;
    }

    public String getInterfaceClassName() {
        return interfaceClassName != null ? interfaceClassName : implementationClassName;
    }

    public String getImplementationClassName() {
        return implementationClassName;
    }

    public boolean isOverride() {
        return override;
    }

    public PreloadMode getPreload() {
        return preload;
    }
}
