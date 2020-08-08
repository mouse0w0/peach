package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileContext;
import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.CompilerImpl;
import com.github.mouse0w0.peach.mcmod.project.McModSettings;
import com.google.gson.JsonObject;

public class AssetsInfoTask implements CompileTask {
    @Override
    public void run(CompileContext context) throws Exception {
        McModSettings projectInfo = context.getData(CompilerImpl.MOD_INFO_KEY);
        JsonObject jo = new JsonObject();
        jo.addProperty("description", projectInfo.getDescription());
        jo.addProperty("pack_format", 3);
        JsonObject jo2 = new JsonObject();
        jo2.add("pack", jo);
        context.getResourcesFiler().write("pack.mcmeta", jo2.toString());
    }
}
