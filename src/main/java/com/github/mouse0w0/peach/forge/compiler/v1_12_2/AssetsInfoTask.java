package com.github.mouse0w0.peach.forge.compiler.v1_12_2;

import com.github.mouse0w0.peach.forge.ForgeModInfo;
import com.github.mouse0w0.peach.forge.compiler.CompileContext;
import com.github.mouse0w0.peach.forge.compiler.CompileTask;
import com.github.mouse0w0.peach.forge.compiler.ForgeCompiler;
import com.google.gson.JsonObject;

public class AssetsInfoTask implements CompileTask {
    @Override
    public void run(CompileContext context) throws Exception {
        ForgeModInfo projectInfo = context.getData(ForgeCompiler.MOD_INFO_KEY);
        JsonObject jo = new JsonObject();
        jo.addProperty("description", projectInfo.getDescription());
        jo.addProperty("pack_format", 3);
        JsonObject jo2 = new JsonObject();
        jo2.add("pack", jo);
        context.getResourcesFiler().write("pack.mcmeta", jo2.toString());
    }
}
