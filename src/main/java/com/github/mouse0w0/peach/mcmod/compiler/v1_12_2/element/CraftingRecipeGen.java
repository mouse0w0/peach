package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.compiler.CompileContext;
import com.github.mouse0w0.peach.mcmod.compiler.CompilerImpl;
import com.github.mouse0w0.peach.mcmod.element.CraftingRecipe;
import com.github.mouse0w0.peach.mcmod.element.ElementFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CraftingRecipeGen extends ElementGen<CraftingRecipe> {

    @Override
    protected void generate(CompileContext context, ElementFile<CraftingRecipe> file) throws Exception {
        CraftingRecipe recipe = file.get();

        JsonObject jo = new JsonObject();
        generateResult(recipe, jo);

        if (recipe.isShapeless()) {
            generateShapeless(recipe, jo);
        } else {
            generateShaped(recipe, jo);
        }

        context.getData(CompilerImpl.MOD_ASSETS_FILER).write("recipes/" + recipe.getId() + ".json", jo.toString());
    }

    private void generateResult(CraftingRecipe recipe, JsonObject jo) {
        ItemStack itemStack = recipe.getOutput();
        JsonObject result = new JsonObject();
        result.addProperty("item", itemStack.getItem().getId());
        result.addProperty("count", itemStack.getAmount());

        jo.add("result", result);
    }

    private void generateShaped(CraftingRecipe recipe, JsonObject jo) {
        jo.addProperty("type", "forge:ore_shaped");
        JsonArray pattern = new JsonArray();
        Map<Item, String> keyMap = new HashMap<>();
        StringBuilder sb = new StringBuilder(10);
        Item[] inputs = recipe.getInputs();
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
        keyMap.forEach((itemToken, s) -> key.add(s, itemToJson(itemToken)));
        jo.add("key", key);
    }

    private void generateShapeless(CraftingRecipe recipe, JsonObject jo) {
        jo.addProperty("type", "forge:ore_shapeless");
        JsonArray ingredients = new JsonArray();
        for (Item input : recipe.getInputs()) {
            if (input != null && !input.isAir()) {
                ingredients.add(itemToJson(input));
            }
        }
        jo.add("ingredients", ingredients);
    }

    private JsonObject itemToJson(Item item) {
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
