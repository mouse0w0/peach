package com.github.mouse0w0.peach.mcmod.generator.v1_12_2;

import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.mcmod.generator.task.Task;
import com.github.mouse0w0.peach.mcmod.project.ModMetadata;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonObject;

public class GenMetadata implements Task {

    @Override
    public void run(Context context) throws Exception {
        ModMetadata metadata = context.getMetadata();
        JsonObject jo = new JsonObject();
        jo.addProperty("modid", metadata.getId());
        jo.addProperty("name", metadata.getName());
        jo.addProperty("description", metadata.getDescription());
        jo.addProperty("version", metadata.getVersion());
        jo.addProperty("mcversion", metadata.getMcVersion());
        jo.addProperty("url", metadata.getUrl());
        jo.addProperty("updateUrl", metadata.getUpdateUrl());
        jo.add("authorList", JsonUtils.stringArray(metadata.getAuthors()));
        jo.addProperty("credits", metadata.getCredits());
        context.getResourcesFiler().write("mcmod.info", JsonUtils.jsonArray(jo).toString());
    }
}
