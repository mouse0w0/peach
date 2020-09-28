package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.util.StringUtils;

import java.nio.file.Path;

public class ElementHelper {

    public static String getElementFileName(Path file) {
        return StringUtils.substringBefore(file.getFileName().toString(), '.');
    }
}
