package com.github.mouse0w0.peach.ui.util;

import javafx.css.PseudoClass;
import javafx.scene.Node;

import java.util.function.Predicate;

public final class Check<T> implements Predicate<T> {
    public static final PseudoClass INVALID_CLASS = PseudoClass.getPseudoClass("invalid");

    private final String message;
    private final Predicate<T> predicate;

    public static <T> Check<T> of(String message, Predicate<T> predicate) {
        return new Check<>(message, predicate);
    }

    public static boolean isInvalid(Node node) {
        return node.getPseudoClassStates().contains(INVALID_CLASS);
    }

    public static void setInvalid(Node node, boolean active) {
        node.pseudoClassStateChanged(INVALID_CLASS, active);
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
