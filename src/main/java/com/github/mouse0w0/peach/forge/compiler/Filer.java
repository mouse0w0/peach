package com.github.mouse0w0.peach.forge.compiler;

import com.github.mouse0w0.peach.util.FileUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Filer {

    private final Path root;

    public Filer(Path root) throws IOException {
        FileUtils.createDirectoriesIfNotExists(root);
        if (!Files.isDirectory(root)) throw new IllegalArgumentException();
        if (!root.isAbsolute()) throw new IllegalArgumentException();
        this.root = root;
    }

    public Path getRoot() {
        return root;
    }

    public OutputStream newOutputStream(String first, String... more) throws IOException {
        return newOutputStream(Paths.get(first, more));
    }

    public OutputStream newOutputStream(Path path) throws IOException {
        Path resolvedPath = root.resolve(path);
        FileUtils.createFileIfNotExists(resolvedPath);
        return Files.newOutputStream(resolvedPath);
    }

    public BufferedWriter newWriter(String first, String... more) throws IOException {
        return newWriter(Paths.get(first, more));
    }

    public BufferedWriter newWriter(Path path) throws IOException {
        return newWriter(path, StandardCharsets.UTF_8);
    }

    public BufferedWriter newWriter(Path path, Charset charset) throws IOException {
        Path resolvedPath = root.resolve(path);
        FileUtils.createFileIfNotExists(resolvedPath);
        return Files.newBufferedWriter(resolvedPath, charset);
    }

    public void write(String path, byte[] bytes) throws IOException {
        write(Paths.get(path), bytes);
    }

    public void write(Path path, byte[] bytes) throws IOException {
        Path resolvedPath = root.resolve(path);
        FileUtils.createFileIfNotExists(resolvedPath);
        Files.write(resolvedPath, bytes);
    }

    public void write(String path, CharSequence... lines) throws IOException {
        write(path, StandardCharsets.UTF_8, lines);
    }

    public void write(Path path, CharSequence... lines) throws IOException {
        write(path, StandardCharsets.UTF_8, lines);
    }

    public void write(String path, Charset charset, CharSequence... lines) throws IOException {
        write(Paths.get(path), charset, lines);
    }

    public void write(Path path, Charset charset, CharSequence... lines) throws IOException {
        try (BufferedWriter writer = newWriter(path, charset)) {
            for (CharSequence line : lines) {
                writer.append(line);
                writer.newLine();
            }
        }
    }

    public void copy(InputStream in, String target, CopyOption... options) throws IOException {
        Files.copy(in, root.resolve(target), options);
    }

    public void copy(InputStream in, Path target, CopyOption... options) throws IOException {
        Files.copy(in, root.resolve(target), options);
    }
}
