package com.github.mouse0w0.peach.forge.compiler.v1_12_2;

import com.github.mouse0w0.peach.forge.ForgeModInfo;
import com.github.mouse0w0.peach.forge.compiler.CompileContext;
import com.github.mouse0w0.peach.forge.compiler.CompileTask;
import com.github.mouse0w0.peach.forge.compiler.ForgeCompiler;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonObject;

public class ModInfoTask implements CompileTask {

    @Override
    public void run(CompileContext context) throws Exception {
        ForgeModInfo projectInfo = context.getData(ForgeCompiler.MOD_INFO_KEY);
        JsonObject jo = new JsonObject();
        jo.addProperty("modid", projectInfo.getId());
        jo.addProperty("name", projectInfo.getName());
        jo.addProperty("description", projectInfo.getDescription());
        jo.addProperty("version", projectInfo.getVersion());
        jo.addProperty("mcversion", projectInfo.getMcVersion());
        jo.add("authorList", JsonUtils.json(projectInfo.getAuthors()));
        context.getResourcesFiler().write("mcmod.info", JsonUtils.jsonArray(jo).toString());
    }
}
