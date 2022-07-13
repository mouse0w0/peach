package com.github.mouse0w0.peach.mcmod.util;

import com.github.mouse0w0.peach.util.ArrayUtils;
import com.github.mouse0w0.peach.util.StringUtils;

import java.util.regex.Pattern;

public final class InformationUtils {
    private static final Pattern SEPARATOR = Pattern.compile("[\r\n]\n?");

    public static String[] spilt(final String str) {
        if (StringUtils.isEmpty(str)) return ArrayUtils.EMPTY_STRING_ARRAY;
        return SEPARATOR.split(str);
    }

    public static String join(final String[] array) {
        if (ArrayUtils.isEmpty(array)) return StringUtils.EMPTY;
        return String.join(System.lineSeparator(), array);
    }

    private InformationUtils() {
    }
}
