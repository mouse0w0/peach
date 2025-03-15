package com.github.mouse0w0.peach.util;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;

@ApiStatus.Internal
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static String substringBeforeLast(String str, int separator) {
        if (isEmpty(str)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
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

    public static String[] splitByLineSeparator(String str) {
        if (str == null) {
            return null;
        }

        int len = str.length();

        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        ArrayList<String> substrings = new ArrayList<>();
        int start = 0;
        int end = 0;
        while (end < len) {
            char c = str.charAt(end);
            if (c == '\r') {
                substrings.add(str.substring(start, end));
                int next = end + 1;
                if (next < len && str.charAt(next) == '\n') {
                    end += 2;
                } else {
                    end++;
                }
                start = end;
            } else if (c == '\n') {
                substrings.add(str.substring(start, end));
                end++;
                start = end;
            } else {
                end++;
            }
        }
        substrings.add(str.substring(start));
        return substrings.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }
}
