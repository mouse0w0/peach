package com.github.mouse0w0.peach.forge.compiler;

import com.github.mouse0w0.peach.data.DataHolder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Path;

public interface CompileContext extends DataHolder {

    Path getSource();

    Path getOutput();

    OutputStream newOutputStream(String path) throws IOException;

    OutputStream newOutputStream(Path path) throws IOException;

    Writer newWriter(String path) throws IOException;

    Writer newWriter(Path path) throws IOException;

    Writer newWriter(Path path, Charset charset) throws IOException;

    void write(Path path, byte[] bytes) throws IOException;

    void write(Path path, String... lines) throws IOException;

    void write(Path path, Charset charset, String... lines) throws IOException;
}
