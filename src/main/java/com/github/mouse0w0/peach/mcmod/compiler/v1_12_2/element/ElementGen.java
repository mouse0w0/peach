package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element;

import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.element.ElementFile;

import java.util.Collection;

public abstract class ElementGen<T> {

    public void generate(Environment environment, Collection<ElementFile<T>> files) throws Exception {
        for (ElementFile<T> file : files) {
            generate(environment, file);
        }
    }

    protected abstract void generate(Environment environment, ElementFile<T> file) throws Exception;
}
