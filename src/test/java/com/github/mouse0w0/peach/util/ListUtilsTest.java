package com.github.mouse0w0.peach.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ListUtilsTest {

    @Test
    void binarySearchInsert() {
        List<Integer> list = new ArrayList<>();
        ListUtils.binarySearchInsert(list, 2);
        ListUtils.binarySearchInsert(list, 7);
        ListUtils.binarySearchInsert(list, 1);
        ListUtils.binarySearchInsert(list, 5);
        ListUtils.binarySearchInsert(list, 6);
        ListUtils.binarySearchInsert(list, 4);
        ListUtils.binarySearchInsert(list, 8);
        ListUtils.binarySearchInsert(list, 0);
        ListUtils.binarySearchInsert(list, 3);
        ListUtils.binarySearchInsert(list, 9);
        assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), list);

        List<Integer> reversedList = new ArrayList<>();
        ListUtils.binarySearchInsert(reversedList, 2, Comparator.reverseOrder());
        ListUtils.binarySearchInsert(reversedList, 7, Comparator.reverseOrder());
        ListUtils.binarySearchInsert(reversedList, 1, Comparator.reverseOrder());
        ListUtils.binarySearchInsert(reversedList, 5, Comparator.reverseOrder());
        ListUtils.binarySearchInsert(reversedList, 6, Comparator.reverseOrder());
        ListUtils.binarySearchInsert(reversedList, 4, Comparator.reverseOrder());
        ListUtils.binarySearchInsert(reversedList, 8, Comparator.reverseOrder());
        ListUtils.binarySearchInsert(reversedList, 0, Comparator.reverseOrder());
        ListUtils.binarySearchInsert(reversedList, 3, Comparator.reverseOrder());
        ListUtils.binarySearchInsert(reversedList, 9, Comparator.reverseOrder());
        assertIterableEquals(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1, 0), reversedList);
    }
}