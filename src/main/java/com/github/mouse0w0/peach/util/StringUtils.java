package com.github.mouse0w0.peach.util;

public final class StringUtils {

    public static String substringBefore(String str, char ch) {
        int i = str.indexOf(ch);
        if (i >= 0) return str.substring(0, i);
        return str;
    }

    public static String substringBeforeLast(String str, char ch) {
        int i = str.lastIndexOf(ch);
        if (i >= 0) return str.substring(0, i);
        return str;
    }

    public static String substringAfter(String str, char ch) {
        int i = str.indexOf(ch);
        if (i >= 0) return str.substring(i + 1);
        return str;
    }

    public static String substringAfterLast(String str, char ch) {
        int i = str.lastIndexOf(ch);
        if (i >= 0) return str.substring(i + 1);
        return str;
    }

    private StringUtils() {
    }
}
