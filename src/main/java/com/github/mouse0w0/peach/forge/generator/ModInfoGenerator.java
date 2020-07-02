package com.github.mouse0w0.peach.forge.generator;

import com.github.mouse0w0.peach.forge.ForgeProjectInfo;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;

public class ModInfoGenerator {

    public static void generate(CompileContext context) throws IOException {
        Path path = context.getData(ForgeCompiler.RESOURCES_STORE_PATH).resolve("mcmod.info");
        ForgeProjectInfo projectInfo = context.getData(ForgeCompiler.PROJECT_INFO_KEY);
        JsonObject jo = new JsonObject();
        jo.addProperty("modid", projectInfo.getId());
        jo.addProperty("name", projectInfo.getName());
        jo.addProperty("description", projectInfo.getDescription());
        jo.addProperty("version", projectInfo.getVersion());
        jo.addProperty("mcversion", projectInfo.getMcVersion());
        jo.add("authorList", JsonUtils.json(projectInfo.getAuthors()));
        context.write(path, JsonUtils.jsonArray(jo).toString());
    }
}
