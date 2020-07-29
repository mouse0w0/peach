package com.github.mouse0w0.peach.forge.compiler.v1_12_2.element;

import com.github.mouse0w0.peach.forge.compiler.CompileContext;
import com.github.mouse0w0.peach.forge.element.ElementFile;

import java.util.Collection;

public abstract class ElementGen<T> {

    public void generate(CompileContext context, Collection<ElementFile<T>> files) throws Exception {
        for (ElementFile<T> file : files) {
            generate(context, file);
        }
    }

    protected abstract void generate(CompileContext context, ElementFile<T> file) throws Exception;
}
