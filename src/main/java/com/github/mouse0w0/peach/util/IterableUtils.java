package com.github.mouse0w0.peach.util;

import java.util.Iterator;
import java.util.function.ObjIntConsumer;

public class IterableUtils {
    public static <E> void forEachWithIndex(Iterable<? extends E> iterable, ObjIntConsumer<? super E> consumer) {
        Iterator<? extends E> iterator = iterable.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            consumer.accept(iterator.next(), i);
        }
    }
}
