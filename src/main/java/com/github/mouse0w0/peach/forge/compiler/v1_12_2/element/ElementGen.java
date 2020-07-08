package com.github.mouse0w0.peach.forge.compiler.v1_12_2.element;

import com.github.mouse0w0.peach.forge.compiler.CompileContext;
import com.github.mouse0w0.peach.forge.element.ElementFile;

public interface ElementGen<T> {

    void generate(CompileContext context, ElementFile<T> file) throws Exception;
}
