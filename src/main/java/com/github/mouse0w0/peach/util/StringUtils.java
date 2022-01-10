package com.github.mouse0w0.peach.util;

public class StringUtils {

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

    public static boolean hasUpperCase(String str) {
        for (int i = 0, length = str.length(); i < length; i++) {
            if (Character.isUpperCase(str.indexOf(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLowerCase(String str) {
        for (int i = 0, length = str.length(); i < length; i++) {
            if (Character.isLowerCase(str.indexOf(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean notEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    private StringUtils() {
    }
}
