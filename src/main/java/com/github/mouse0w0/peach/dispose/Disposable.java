package com.github.mouse0w0.peach.dispose;

public interface Disposable {
    void dispose();

    interface Default extends Disposable {
        @Override
        default void dispose() {
        }
    }
}
