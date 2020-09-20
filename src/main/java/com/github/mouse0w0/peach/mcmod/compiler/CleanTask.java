package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.util.FileUtils;

public class CleanTask implements CompileTask {
    @Override
    public void run(Environment environment) throws Exception {
        FileUtils.deleteDirectory(environment.getOutputDirectory());
    }
}
