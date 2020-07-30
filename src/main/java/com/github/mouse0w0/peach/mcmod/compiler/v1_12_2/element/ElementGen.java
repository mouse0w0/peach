package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element;

import com.github.mouse0w0.peach.mcmod.compiler.CompileContext;
import com.github.mouse0w0.peach.mcmod.element.ElementFile;

import java.util.Collection;

public abstract class ElementGen<T> {

    public void generate(CompileContext context, Collection<ElementFile<T>> files) throws Exception {
        for (ElementFile<T> file : files) {
            generate(context, file);
        }
    }

    protected abstract void generate(CompileContext context, ElementFile<T> file) throws Exception;
}
