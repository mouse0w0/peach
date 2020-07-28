package com.github.mouse0w0.peach.forge.compiler.v1_12_2.element;

import com.github.mouse0w0.peach.forge.Item;
import com.github.mouse0w0.peach.forge.ItemStack;
import com.github.mouse0w0.peach.forge.compiler.CompileContext;
import com.github.mouse0w0.peach.forge.compiler.ForgeCompiler;
import com.github.mouse0w0.peach.forge.element.CraftingRecipe;
import com.github.mouse0w0.peach.forge.element.ElementFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CraftingRecipeGen implements ElementGen<CraftingRecipe> {

    public void generate(CompileContext context, ElementFile<CraftingRecipe> file) throws Exception {
        CraftingRecipe recipe = file.get();

        JsonObject jo = new JsonObject();
        jo.add("result", json(recipe.getOutput()));

        Item[] inputs = recipe.getInputs();
        if (recipe.isShapeless()) {
            jo.addProperty("type", "forge:ore_shapeless");
            JsonArray ingredients = new JsonArray();
            for (Item input : inputs) {
                if (input != null && !input.isAir()) {
                    ingredients.add(json(input));
                }
            }
            jo.add("ingredients", ingredients);
        } else {
            jo.addProperty("type", "forge:ore_shaped");
            JsonArray pattern = new JsonArray();
            Map<Item, String> keyMap = new HashMap<>();
            StringBuilder sb = new StringBuilder(10);
            for (int i = 0; i < 9; i++) {
                Item input = inputs[i];
                if (input != null && !input.isAir()) {
                    sb.append(keyMap.computeIfAbsent(input, $ -> Character.toString((char) ('A' + keyMap.size()))));
                } else {
                    sb.append(" ");
                }
            }
            for (int i = 0; i < 3; i++) {
                pattern.add(sb.substring(i * 3, i * 3 + 3));
            }
            jo.add("pattern", pattern);

            JsonObject key = new JsonObject();
            keyMap.forEach((itemToken, s) -> key.add(s, json(itemToken)));
            jo.add("key", key);
        }

        context.getData(ForgeCompiler.MOD_ASSETS_FILER).write("recipes/" + recipe.getId() + ".json", jo.toString());
    }

    private JsonObject json(ItemStack itemStack) {
        JsonObject result = new JsonObject();
        result.addProperty("item", itemStack.getItem().getId());
        result.addProperty("count", itemStack.getAmount());
        return result;
    }

    private JsonObject json(Item item) {
        JsonObject result = new JsonObject();
        if (item.isOreDict()) {
            result.addProperty("type", "forge:ore_dict");
            result.addProperty("ore", item.getId());
        } else {
            result.addProperty("item", item.getId());
            result.addProperty("data", item.getMetadata());
        }
        return result;
    }
}
