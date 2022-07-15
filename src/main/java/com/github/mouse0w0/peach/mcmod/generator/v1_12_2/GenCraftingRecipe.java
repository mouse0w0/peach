package com.github.mouse0w0.peach.mcmod.generator.v1_12_2;

import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.element.ElementTypes;
import com.github.mouse0w0.peach.mcmod.element.impl.MECraftingRecipe;
import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.mcmod.generator.task.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class GenCraftingRecipe implements Task {

    @Override
    public void run(Context context) throws Exception {
        for (MECraftingRecipe crafting : context.getElements(ElementTypes.CRAFTING_RECIPE)) {
            JsonObject jo = new JsonObject();
            if (crafting.isShapeless()) {
                generateShapeless(crafting, jo);
            } else {
                generateShaped(crafting, jo);
            }
            generateResult(crafting, jo);
            context.getAssetsFiler().write("recipes/" + crafting.getIdentifier() + ".json", jo.toString());
        }
    }

    private void generateResult(MECraftingRecipe recipe, JsonObject jo) {
        ItemStack itemStack = recipe.getOutput();
        JsonObject result = new JsonObject();
        result.addProperty("item", itemStack.getId());
        result.addProperty("data", itemStack.getMetadata());
        result.addProperty("count", itemStack.getAmount());
        jo.add("result", result);
    }

    private void generateShaped(MECraftingRecipe recipe, JsonObject jo) {
        jo.addProperty("type", "forge:ore_shaped");
        JsonArray pattern = new JsonArray();
        Map<ItemRef, Character> keyMap = new HashMap<>();
        for (String s : getPattern(recipe.getInputs(), keyMap)) {
            pattern.add(s);
        }
        jo.add("pattern", pattern);

        JsonObject key = new JsonObject();
        keyMap.forEach((itemToken, c) -> key.add(c.toString(), itemRefToJson(itemToken)));
        jo.add("key", key);
    }

    private String[] getPattern(ItemRef[] inputs, Map<ItemRef, Character> keyMap) {
        StringBuilder sb = new StringBuilder(9);
        for (int i = 0; i < 9; i++) {
            ItemRef input = inputs[i];
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

    private void generateShapeless(MECraftingRecipe recipe, JsonObject jo) {
        jo.addProperty("type", "forge:ore_shapeless");
        JsonArray ingredients = new JsonArray();
        for (ItemRef input : recipe.getInputs()) {
            if (input != null && !input.isAir()) {
                ingredients.add(itemRefToJson(input));
            }
        }
        jo.add("ingredients", ingredients);
    }

    private JsonObject itemRefToJson(ItemRef itemRef) {
        JsonObject result = new JsonObject();
        if (itemRef.isOreDict()) {
            result.addProperty("type", "forge:ore_dict");
            result.addProperty("ore", itemRef.getId());
        } else {
            result.addProperty("item", itemRef.getId());
            result.addProperty("data", itemRef.getMetadata());
        }
        return result;
    }
}
