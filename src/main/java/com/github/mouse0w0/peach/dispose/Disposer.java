package com.github.mouse0w0.peach.dispose;

import org.jetbrains.annotations.NotNull;

public final class Disposer {
    private static final ObjectTree TREE = new ObjectTree();

    @NotNull
    public static Disposable newDisposable() {
        return new Disposable() {
            @Override
            public void dispose() {
                // Nothing to do.
            }

            @Override
            public String toString() {
                return "Disposable";
            }
        };
    }

    public static void register(@NotNull Disposable parent, @NotNull Disposable child) {
        TREE.register(parent, child);
    }

    public static void dispose(Disposable disposable) {
        TREE.dispose(disposable, true);
    }

    public static void disposeIfRegistered(Disposable disposable) {
        TREE.dispose(disposable, false);
    }

    public static void checkAllDisposed() {
        TREE.checkAllDisposed();
    }

    private Disposer() {
    }
}
