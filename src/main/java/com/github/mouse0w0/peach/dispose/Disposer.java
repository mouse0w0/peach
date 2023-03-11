package com.github.mouse0w0.peach.dispose;

import javax.annotation.Nonnull;

public final class Disposer {
    private static final ObjectTree TREE = new ObjectTree();

    @Nonnull
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

    public static void register(@Nonnull Disposable parent, @Nonnull Disposable child) {
        TREE.register(parent, child);
    }

    public static void dispose(Disposable disposable) {
        TREE.dispose(disposable);
    }

    public static void disposeChildren(Disposable disposable) {
        TREE.disposeChildren(disposable);
    }

    public static void checkAllDisposed() {
        TREE.checkAllDisposed();
    }

    private Disposer() {
    }
}
