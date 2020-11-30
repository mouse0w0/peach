package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.compiler.Compiler;

import java.util.Collection;

public abstract class Generator<T> {

    public void generate(Compiler compiler, Collection<T> elements) throws Exception {
        before(compiler, elements);
        for (T element : elements) {
            generate(compiler, element);
        }
        after(compiler, elements);
    }

    protected void before(Compiler compiler, Collection<T> elements) throws Exception {
        // Nothing to do
    }

    protected void after(Compiler compiler, Collection<T> elements) throws Exception {
        // Nothing to do
    }

    protected abstract void generate(Compiler compiler, T element) throws Exception;
}
