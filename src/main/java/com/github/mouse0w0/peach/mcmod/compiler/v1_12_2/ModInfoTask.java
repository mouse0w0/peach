package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonObject;

public class ModInfoTask implements CompileTask {

    @Override
    public void run(Environment environment) throws Exception {
        McModMetadata modSettings = environment.getMetadata();
        JsonObject jo = new JsonObject();
        jo.addProperty("modid", modSettings.getId());
        jo.addProperty("name", modSettings.getName());
        jo.addProperty("description", modSettings.getDescription());
        jo.addProperty("version", modSettings.getVersion());
        jo.addProperty("mcversion", modSettings.getMcVersion());
        jo.add("authorList", JsonUtils.json(modSettings.getAuthors()));
        environment.getResourcesFiler().write("mcmod.info", JsonUtils.jsonArray(jo).toString());
    }
}
