package com.github.mouse0w0.peach.forge.generator;

import com.github.mouse0w0.peach.forge.ForgeProjectInfo;
import com.google.gson.JsonArray;
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
        jo.addProperty("version", projectInfo.getVersion());
        jo.addProperty("mcversion", projectInfo.getMcVersion());
        JsonArray ja = new JsonArray();
        ja.add(jo);
        context.write(path, ja.toString());
    }
}
