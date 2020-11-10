package com.github.mouse0w0.peach.extension;

public interface ExtensionDescriptor extends Comparable<ExtensionDescriptor> {
    Object newInstance();
}
