package com.github.mouse0w0.peach.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StringUtilsTest {

    @Test
    void splitByLineSeparator() {
        assertNull(StringUtils.splitByLineSeparator(null));
        assertArrayEquals(ArrayUtils.EMPTY_STRING_ARRAY, StringUtils.splitByLineSeparator(""));
        assertArrayEquals(new String[]{"abc"}, StringUtils.splitByLineSeparator("abc"));
        assertArrayEquals(new String[]{"abc", "abc"}, StringUtils.splitByLineSeparator("abc\r\nabc"));
        assertArrayEquals(new String[]{"abc", ""}, StringUtils.splitByLineSeparator("abc\r\n"));
        assertArrayEquals(new String[]{"abc", "abc"}, StringUtils.splitByLineSeparator("abc\nabc"));
        assertArrayEquals(new String[]{"abc", ""}, StringUtils.splitByLineSeparator("abc\n"));
        assertArrayEquals(new String[]{"abc", "abc"}, StringUtils.splitByLineSeparator("abc\rabc"));
        assertArrayEquals(new String[]{"abc", ""}, StringUtils.splitByLineSeparator("abc\r"));
    }
}