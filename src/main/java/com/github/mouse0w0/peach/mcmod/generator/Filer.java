package com.github.mouse0w0.peach.mcmod.generator;

import com.github.mouse0w0.peach.util.FileUtils;

import java.io.BufferedWriter;
import java.io.IOException;
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

    public Path resolve(String first, String... more) {
        return root.resolve(Paths.get(first, more));
    }

    public Path resolve(Path other) {
        return root.resolve(other);
    }

    public OutputStream newOutputStream(String first, String... more) throws IOException {
        return newOutputStream(Paths.get(first, more));
    }

    public OutputStream newOutputStream(Path path) throws IOException {
        Path resolvedPath = root.resolve(path);
        return Files.newOutputStream(resolvedPath);
    }

    public BufferedWriter newWriter(String first, String... more) throws IOException {
        return newWriter(Paths.get(first, more));
    }

    public BufferedWriter newWriter(Path path) throws IOException {
        return newWriter(path, StandardCharsets.UTF_8);
    }

    public BufferedWriter newWriter(Path path, Charset charset) throws IOException {
        return Files.newBufferedWriter(root.resolve(path), charset);
    }

    public void write(String path, byte[] bytes) throws IOException {
        write(Paths.get(path), bytes);
    }

    public void write(Path path, byte[] bytes) throws IOException {
        Files.write(root.resolve(path), bytes);
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

    public void copy(Path source, String target, CopyOption... options) throws IOException {
        FileUtils.copyIfNotExists(source, root.resolve(target), options);
    }

    public void copy(Path source, Path target, CopyOption... options) throws IOException {
        FileUtils.copyIfNotExists(source, root.resolve(target), options);
    }
}
