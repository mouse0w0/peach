package com.github.mouse0w0.peach.ui.util;

import java.util.function.Predicate;

public class CheckItem<T> implements Comparable<CheckItem<T>> {
    private final Predicate<T> predicate;
    private final NotificationLevel level;
    private final String message;

    public CheckItem(Predicate<T> predicate, NotificationLevel level, String message) {
        this.predicate = predicate;
        this.level = level;
        this.message = message;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    public NotificationLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public boolean test(T value) {
        return predicate.test(value);
    }

    @Override
    public int compareTo(CheckItem<T> o) {
        return level.compareTo(o.level);
    }
}
