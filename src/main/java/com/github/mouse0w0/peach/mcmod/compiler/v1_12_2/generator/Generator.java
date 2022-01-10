package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.compiler.Context;

import java.util.Collection;

public abstract class Generator<T> {

    public void generate(Context context, Collection<T> elements) throws Exception {
        before(context, elements);
        for (T element : elements) {
            generate(context, element);
        }
        after(context, elements);
    }

    protected void before(Context context, Collection<T> elements) throws Exception {
        // Nothing to do
    }

    protected void after(Context context, Collection<T> elements) throws Exception {
        // Nothing to do
    }

    protected abstract void generate(Context context, T element) throws Exception;
}
