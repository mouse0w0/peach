package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.element.Element;

import java.util.Collection;

public abstract class Generator<T> {

    public void generate(Environment environment, Collection<Element<T>> files) throws Exception {
        for (Element<T> file : files) {
            generate(environment, file);
        }
    }

    protected abstract void generate(Environment environment, Element<T> file) throws Exception;
}
