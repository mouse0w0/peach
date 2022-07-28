package com.github.mouse0w0.peach.util;

import java.util.stream.Stream;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String substringBeforeLast(final String str, final int separator) {
        if (isEmpty(str)) {
            return str;
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static boolean hasUpperCase(final String str) {
        for (int i = 0, length = str.length(); i < length; i++) {
            if (Character.isUpperCase(str.indexOf(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLowerCase(final String str) {
        for (int i = 0, length = str.length(); i < length; i++) {
            if (Character.isLowerCase(str.indexOf(i))) {
                return true;
            }
        }
        return false;
    }

    public static String join(final Stream<?> stream, final char separator) {
        return join(stream.iterator(), separator);
    }

    public static String join(final Stream<?> stream, final String separator) {
        return join(stream.iterator(), separator);
    }

    private StringUtils() {
    }
}
