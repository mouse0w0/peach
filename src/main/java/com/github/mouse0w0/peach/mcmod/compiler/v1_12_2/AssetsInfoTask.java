package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.google.gson.JsonObject;

public class AssetsInfoTask implements CompileTask {
    @Override
    public void run(Environment environment) throws Exception {
        McModMetadata modSettings = environment.getMetadata();
        JsonObject jo = new JsonObject();
        jo.addProperty("description", modSettings.getDescription());
        jo.addProperty("pack_format", 3);
        JsonObject jo2 = new JsonObject();
        jo2.add("pack", jo);
        environment.getResourcesFiler().write("pack.mcmeta", jo2.toString());
    }
}
