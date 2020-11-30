package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.Compiler;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.google.gson.JsonObject;

public class AssetsInfoTask implements CompileTask {
    @Override
    public void run(Compiler compiler) throws Exception {
        McModMetadata modSettings = compiler.getMetadata();
        JsonObject jo = new JsonObject();
        jo.addProperty("description", modSettings.getDescription());
        jo.addProperty("pack_format", 3);
        JsonObject jo2 = new JsonObject();
        jo2.add("pack", jo);
        compiler.getResourcesFiler().write("pack.mcmeta", jo2.toString());
    }
}
