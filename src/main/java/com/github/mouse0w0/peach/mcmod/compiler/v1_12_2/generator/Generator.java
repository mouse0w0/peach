package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.element.Element;

import java.util.Collection;

public abstract class Generator<T> {

    public void generate(Environment environment, Collection<Element<T>> elements) throws Exception {
        for (Element<T> element : elements) {
            generate(environment, element);
        }
    }

    protected abstract void generate(Environment environment, Element<T> element) throws Exception;
}
