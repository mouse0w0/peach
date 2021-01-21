package com.github.mouse0w0.peach.javafx.util;

import java.util.function.Predicate;

public class Check<T> implements Comparable<Check<T>> {
    private final Predicate<T> predicate;
    private final NotificationLevel level;
    private final String message;

    public Check(Predicate<T> predicate, NotificationLevel level, String message) {
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
    public int compareTo(Check<T> o) {
        return level.compareTo(o.level);
    }
}
