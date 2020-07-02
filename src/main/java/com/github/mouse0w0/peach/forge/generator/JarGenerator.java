package com.github.mouse0w0.peach.forge.generator;

import com.github.mouse0w0.peach.forge.ForgeProjectInfo;
import com.github.mouse0w0.peach.util.ZipUtils;

import java.io.IOException;
import java.nio.file.Path;

public class JarGenerator {

    public static void generate(CompileContext context) throws IOException {
        ForgeProjectInfo projectInfo = context.getData(ForgeCompiler.PROJECT_INFO_KEY);
        Path artifacts = context.getData(ForgeCompiler.ARTIFACTS_STORE_PATH).resolve(projectInfo.getId() + "-" + projectInfo.getVersion() + ".jar");
        ZipUtils.zip(artifacts,
                context.getData(ForgeCompiler.CLASSES_STORE_PATH),
                context.getData(ForgeCompiler.RESOURCES_STORE_PATH));
    }
}
