package com.github.mouse0w0.peach.forge.compiler;

import com.github.mouse0w0.peach.forge.ForgeProjectInfo;
import com.github.mouse0w0.peach.util.ZipUtils;

import java.nio.file.Path;

public class JarTask implements CompileTask {

    @Override
    public void run(CompileContext context) throws Exception {
        ForgeProjectInfo projectInfo = context.getData(ForgeCompiler.PROJECT_INFO_KEY);
        Path artifacts = context.getData(ForgeCompiler.ARTIFACTS_STORE_PATH).resolve(projectInfo.getId() + "-" + projectInfo.getVersion() + ".jar");
        ZipUtils.zip(artifacts,
                context.getData(ForgeCompiler.CLASSES_STORE_PATH),
                context.getData(ForgeCompiler.RESOURCES_STORE_PATH));
    }
}
