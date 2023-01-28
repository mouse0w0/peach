package com.github.mouse0w0.peach.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class Validate {
    public static <T> T notNull(T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }

    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static <T> T notNull(T object, String message, Object... args) {
        if (object == null) {
            throw new NullPointerException(String.format(message, args));
        }
        return object;
    }

    public static <T extends CharSequence> T notEmpty(T cs) {
        if (cs == null) {
            throw new NullPointerException();
        }
        if (cs.length() == 0) {
            throw new IllegalArgumentException();
        }
        return cs;
    }

    public static <T extends CharSequence> T notEmpty(T cs, String message) {
        if (cs == null) {
            throw new NullPointerException(message);
        }
        if (cs.length() == 0) {
            throw new IllegalArgumentException(message);
        }
        return cs;
    }

    public static <T extends CharSequence> T notEmpty(T cs, String message, Object... args) {
        if (cs == null) {
            throw new NullPointerException(String.format(message, args));
        }
        if (cs.length() == 0) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return cs;
    }

    public static <T extends Collection<?>> T notEmpty(T collection) {
        if (collection == null) {
            throw new NullPointerException();
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return collection;
    }

    public static <T extends Collection<?>> T notEmpty(T collection, String message) {
        if (collection == null) {
            throw new NullPointerException(message);
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return collection;
    }

    public static <T extends Collection<?>> T notEmpty(T collection, String message, Object... args) {
        if (collection == null) {
            throw new NullPointerException(String.format(message, args));
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return collection;
    }

    public static <T extends Map<?, ?>> T notEmpty(T map) {
        if (map == null) {
            throw new NullPointerException();
        }
        if (map.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return map;
    }

    public static <T extends Map<?, ?>> T notEmpty(T map, String message) {
        if (map == null) {
            throw new NullPointerException(message);
        }
        if (map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return map;
    }

    public static <T extends Map<?, ?>> T notEmpty(T map, String message, Object... args) {
        if (map == null) {
            throw new NullPointerException(String.format(message, args));
        }
        if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return map;
    }

    public static <T> T[] notEmpty(T[] array) {
        if (array == null) {
            throw new NullPointerException();
        }
        if (array.length == 0) {
            throw new IllegalArgumentException();
        }
        return array;
    }

    public static <T> T[] notEmpty(T[] array, String message) {
        if (array == null) {
            throw new NullPointerException(message);
        }
        if (array.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static <T> T[] notEmpty(T[] array, String message, Object... args) {
        if (array == null) {
            throw new NullPointerException(String.format(message, args));
        }
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return array;
    }

    public static <T extends CharSequence> T notBlank(T cs) {
        if (cs == null) {
            throw new NullPointerException();
        }
        if (StringUtils.isBlank(cs)) {
            throw new IllegalArgumentException();
        }
        return cs;
    }

    public static <T extends CharSequence> T notBlank(T cs, String message) {
        if (cs == null) {
            throw new NullPointerException(message);
        }
        if (StringUtils.isBlank(cs)) {
            throw new IllegalArgumentException(message);
        }
        return cs;
    }

    public static <T extends CharSequence> T notBlank(T cs, String message, Object... args) {
        if (cs == null) {
            throw new NullPointerException(String.format(message, args));
        }
        if (StringUtils.isBlank(cs)) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return cs;
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable) {
        if (iterable == null) {
            throw new NullPointerException();
        }
        for (Iterator<?> it = iterable.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                throw new IllegalArgumentException();
            }
        }
        return iterable;
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable, String message) {
        if (iterable == null) {
            throw new NullPointerException(message);
        }
        for (Iterator<?> it = iterable.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                throw new IllegalArgumentException(message);
            }
        }
        return iterable;
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable, String message, Object... args) {
        if (iterable == null) {
            throw new NullPointerException(String.format(message, args));
        }
        for (Iterator<?> it = iterable.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                throw new IllegalArgumentException(String.format(message, args));
            }
        }
        return iterable;
    }

    public static <T> T[] noNullElements(T[] array) {
        if (array == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        return array;
    }

    public static <T> T[] noNullElements(T[] array, String message) {
        if (array == null) {
            throw new NullPointerException(message);
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalArgumentException(message);
            }
        }
        return array;
    }

    public static <T> T[] noNullElements(T[] array, String message, Object... args) {
        if (array == null) {
            throw new NullPointerException(String.format(message, args));
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalArgumentException(String.format(message, args));
            }
        }
        return array;
    }

    public static double notNaN(double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    public static double notNaN(double value, String message) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static double notNaN(double value, String message, Object... args) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return value;
    }

    public static float notNaN(float value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    public static float notNaN(float value, String message) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static float notNaN(float value, String message, Object... args) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return value;
    }

    public static double finite(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    public static double finite(double value, String message) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static double finite(double value, String message, Object... args) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return value;
    }

    public static float finite(float value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    public static float finite(float value, String message) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static float finite(float value, String message, Object... args) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return value;
    }
}
