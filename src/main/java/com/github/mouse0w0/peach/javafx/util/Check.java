package com.github.mouse0w0.peach.javafx.util;

import java.util.function.Predicate;

public final class Check<T> implements Predicate<T> {
    public static final String INVALID_STYLE_CLASS = "invalid";

    private final String message;
    private final Predicate<T> predicate;

    public static <T> Check<T> of(String message, Predicate<T> predicate) {
        return new Check<>(message, predicate);
    }

    private Check(String message, Predicate<T> predicate) {
        this.message = message;
        this.predicate = predicate;
    }

    public String getMessage() {
        return message;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    @Override
    public boolean test(T value) {
        return predicate.test(value);
    }
}
