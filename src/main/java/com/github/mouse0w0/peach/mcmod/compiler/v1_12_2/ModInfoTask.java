package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonObject;

public class ModInfoTask implements CompileTask {

    @Override
    public void run(Context context) throws Exception {
        McModMetadata metadata = context.getMetadata();
        JsonObject jo = new JsonObject();
        jo.addProperty("modid", metadata.getId());
        jo.addProperty("name", metadata.getName());
        jo.addProperty("description", metadata.getDescription());
        jo.addProperty("version", metadata.getVersion());
        jo.addProperty("mcversion", metadata.getMcVersion());
        jo.add("authorList", JsonUtils.jsonStringArray(metadata.getAuthors()));
        context.getResourcesFiler().write("mcmod.info", JsonUtils.jsonArray(jo).toString());
    }
}
