package com.github.mouse0w0.peach.dispose;

import javax.annotation.Nonnull;

public final class Disposer {
    private static final ObjectTree tree = new ObjectTree();

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
        tree.register(parent, child);
    }

    public static void dispose(Disposable disposable) {
        tree.dispose(disposable);
    }

    public static void disposeChildren(Disposable disposable) {
        tree.disposeChildren(disposable);
    }

    public static void checkAllDisposed() {
        tree.checkAllDisposed();
    }

    private Disposer() {
    }
}
