package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.compiler.Environment;

import java.util.Collection;

public abstract class Generator<T> {

    public void generate(Environment environment, Collection<T> elements) throws Exception {
        before(environment, elements);
        for (T element : elements) {
            generate(environment, element);
        }
        after(environment, elements);
    }

    protected void before(Environment environment, Collection<T> elements) throws Exception {
        // Nothing to do
    }

    protected void after(Environment environment, Collection<T> elements) throws Exception {
        // Nothing to do
    }

    protected abstract void generate(Environment environment, T element) throws Exception;
}
