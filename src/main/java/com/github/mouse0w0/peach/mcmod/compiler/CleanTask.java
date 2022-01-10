package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.util.FileUtils;

public class CleanTask implements CompileTask {
    @Override
    public void run(Context context) throws Exception {
        FileUtils.deleteDirectory(context.getOutputFolder());
    }
}
