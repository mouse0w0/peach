package com.github.mouse0w0.peach.mcmod.util;

public class ASMUtils {

    public static String getInternalName(String className) {
        return className.replace('.', '/');
    }

    public static String getInternalName(String packageName, String className) {
        return packageName.replace('.', '/') + "/" + className;
    }

    public static String normalizeClassName(String className) {
        return Character.toUpperCase(className.charAt(0)) + className.substring(1);
    }

    public static String getDescriptor(String internalClassName) {
        return "L" + internalClassName + ";";
    }

    public static String getDescriptor(String packageName, String className) {
        return "L" + getInternalName(packageName, className) + ";";
    }
}
