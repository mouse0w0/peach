package com.github.mouse0w0.peach.forge.compiler;

import com.github.mouse0w0.peach.data.DataHolder;

import java.nio.file.Path;

public interface CompileContext extends DataHolder {

    Path getSourceDirectory();

    Path getOutputDirectory();

    Filer getClassesFiler();

    Filer getResourcesFiler();
}
