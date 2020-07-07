package com.github.mouse0w0.peach.util;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

    public static <T, U> void biForEach(T[] t, U[] u, BiConsumer<T, U> consumer) {
        biForEach(t, u, Math.min(t.length, u.length), consumer);
    }

    public static <T, U> void biForEach(T[] t, U[] u, int length, BiConsumer<T, U> consumer) {
        for (int i = 0; i < length; i++) {
            consumer.accept(t[i], u[i]);
        }
    }

    public static <T, R> R[] map(T[] t, Function<? super T, ? extends R> mapper, IntFunction<R[]> generator) {
        final int length = t.length;
        final R[] result = generator.apply(length);
        for (int i = 0; i < length; i++) {
            result[i] = mapper.apply(t[i]);
        }
        return result;
    }
}
