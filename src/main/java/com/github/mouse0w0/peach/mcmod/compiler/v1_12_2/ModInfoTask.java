package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileContext;
import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.CompilerImpl;
import com.github.mouse0w0.peach.mcmod.data.McModSettings;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonObject;

public class ModInfoTask implements CompileTask {

    @Override
    public void run(CompileContext context) throws Exception {
        McModSettings projectInfo = context.getData(CompilerImpl.MOD_INFO_KEY);
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
