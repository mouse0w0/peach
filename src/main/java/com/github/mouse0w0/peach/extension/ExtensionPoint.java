package com.github.mouse0w0.peach.extension;

import com.github.mouse0w0.peach.plugin.Plugin;

import java.util.List;

public interface ExtensionPoint<T> {
    Plugin getPlugin();

    String getName();

    List<T> getExtensions();
}
