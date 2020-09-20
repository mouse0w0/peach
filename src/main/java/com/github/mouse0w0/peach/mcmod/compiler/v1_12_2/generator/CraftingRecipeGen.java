package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.impl.CraftingRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CraftingRecipeGen extends Generator<CraftingRecipe> {

    @Override
    protected void generate(Environment environment, Element<CraftingRecipe> element) throws Exception {
        CraftingRecipe recipe = element.get();

        JsonObject jo = new JsonObject();
        generateResult(recipe, jo);

        if (recipe.isShapeless()) {
            generateShapeless(recipe, jo);
        } else {
            generateShaped(recipe, jo);
        }

        environment.getAssetsFiler().write("recipes/" + recipe.getId() + ".json", jo.toString());
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
        Map<Item, Character> keyMap = new HashMap<>();
        for (String s : getPattern(recipe.getInputs(), keyMap)) {
            pattern.add(s);
        }
        jo.add("pattern", pattern);

        JsonObject key = new JsonObject();
        keyMap.forEach((itemToken, c) -> key.add(c.toString(), itemToJson(itemToken)));
        jo.add("key", key);
    }

    private String[] getPattern(Item[] inputs, Map<Item, Character> keyMap) {
        StringBuilder sb = new StringBuilder(9);
        for (int i = 0; i < 9; i++) {
            Item input = inputs[i];
            if (!input.isAir()) {
                sb.append(keyMap.computeIfAbsent(input, $ -> (char) ('A' + keyMap.size())));
            } else {
                sb.append(" ");
            }
        }

        String[] pattern = new String[3];
        for (int i = 0; i < 3; i++) {
            pattern[i] = sb.substring(i * 3, i * 3 + 3);
        }
        return optimizePattern(pattern);
    }

    public static String[] optimizePattern(String[] pattern) {
        if ("   ".equals(pattern[0])) {
            if (pattern[1].charAt(0) == ' ' && pattern[2].charAt(0) == ' ') {
                return new String[]{pattern[1].substring(1), pattern[2].substring(1)};
            }
            if (pattern[1].charAt(2) == ' ' && pattern[2].charAt(2) == ' ') {
                return new String[]{pattern[1].substring(0, 2), pattern[2].substring(0, 2)};
            }
        } else if ("   ".equals(pattern[2])) {
            if (pattern[0].charAt(0) == ' ' && pattern[1].charAt(0) == ' ') {
                return new String[]{pattern[0].substring(1), pattern[1].substring(1)};
            }
            if (pattern[0].charAt(2) == ' ' && pattern[1].charAt(2) == ' ') {
                return new String[]{pattern[0].substring(0, 2), pattern[1].substring(0, 2)};
            }
        }
        return pattern;
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
