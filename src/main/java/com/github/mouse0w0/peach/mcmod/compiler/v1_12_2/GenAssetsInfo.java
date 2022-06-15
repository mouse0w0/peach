package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.compiler.task.Task;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.google.gson.JsonObject;

public class GenAssetsInfo implements Task {
    @Override
    public void run(Context context) throws Exception {
        McModMetadata metadata = context.getMetadata();
        JsonObject jo = new JsonObject();
        jo.addProperty("description", metadata.getId() + " assets");
        jo.addProperty("pack_format", 3);
        JsonObject jo2 = new JsonObject();
        jo2.add("pack", jo);
        context.getResourcesFiler().write("pack.mcmeta", jo2.toString());
    }
}
