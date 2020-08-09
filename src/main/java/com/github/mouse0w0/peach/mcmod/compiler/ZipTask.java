package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.util.ZipUtils;

import java.nio.file.Path;

public class ZipTask implements CompileTask {

    private final Path outputFile;
    private final Path[] inputs;

    public ZipTask(Path outputFile, Path... inputs) {
        this.outputFile = outputFile;
        this.inputs = inputs;
    }

    @Override
    public void run(Environment environment) throws Exception {
        ZipUtils.zip(outputFile, inputs);
    }
}
