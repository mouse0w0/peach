package com.github.mouse0w0.peach.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ListUtils {
    public static <E> List<E> filterCollect(Iterable<? extends E> iterable, Predicate<? super E> filter) {
        return filterCollect(iterable, filter, new ArrayList<>());
    }

    public static <E> List<E> filterCollect(Iterable<? extends E> iterable, Predicate<? super E> filter, List<E> result) {
        for (E e : iterable) {
            if (filter.test(e)) {
                result.add(e);
            }
        }
        return result;
    }
}
