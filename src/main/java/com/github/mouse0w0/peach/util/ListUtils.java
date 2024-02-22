package com.github.mouse0w0.peach.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtils {
    public static <E> List<E> filter(Iterable<? extends E> iterable, Predicate<? super E> filter) {
        return filter(iterable, filter, new ArrayList<>());
    }

    public static <E> List<E> filter(Iterable<? extends E> iterable, Predicate<? super E> filter, List<E> result) {
        for (E e : iterable) {
            if (filter.test(e)) {
                result.add(e);
            }
        }
        return result;
    }

    public static <T, R> List<R> map(Iterable<? extends T> iterable, Function<? super T, ? extends R> mapper) {
        return map(iterable, mapper, new ArrayList<>());
    }

    public static <T, R> List<R> map(Iterable<? extends T> iterable, Function<? super T, ? extends R> mapper, List<R> result) {
        for (T t : iterable) {
            result.add(mapper.apply(t));
        }
        return result;
    }

    public static <E extends Comparable<E>> void binarySearchInsert(List<E> list, E element) {
        int left = 0;
        int right = list.size();
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (element.compareTo(list.get(mid)) < 0) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        list.add(left, element);
    }

    public static <E> void binarySearchInsert(List<E> list, E element, Comparator<E> comparator) {
        int left = 0;
        int right = list.size();
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (comparator.compare(element, list.get(mid)) < 0) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        list.add(left, element);
    }
}
