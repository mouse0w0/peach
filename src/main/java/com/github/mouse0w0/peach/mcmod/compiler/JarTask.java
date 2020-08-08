package com.github.mouse0w0.peach.mcmod.compiler;

import com.github.mouse0w0.peach.mcmod.project.McModSettings;
import com.github.mouse0w0.peach.util.ZipUtils;

import java.nio.file.Path;

public class JarTask implements CompileTask {

    private final Path outputDirectory;

    public JarTask(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void run(CompileContext context) throws Exception {
        McModSettings projectInfo = context.getData(CompilerImpl.MOD_INFO_KEY);
        Path artifacts = outputDirectory.resolve(projectInfo.getId() + "-" + projectInfo.getVersion() + ".jar");
        ZipUtils.zip(artifacts,
                context.getOutputDirectory().resolve("classes"),
                context.getOutputDirectory().resolve("resources"));
    }
}
