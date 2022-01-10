package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.google.gson.JsonObject;

public class AssetsInfoTask implements CompileTask {
    @Override
    public void run(Context context) throws Exception {
        McModMetadata metadata = context.getMetadata();
        JsonObject jo = new JsonObject();
        jo.addProperty("description", metadata.getDescription());
        jo.addProperty("pack_format", 3);
        JsonObject jo2 = new JsonObject();
        jo2.add("pack", jo);
        context.getResourcesFiler().write("pack.mcmeta", jo2.toString());
    }
}
