package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.GenCraftingRecipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GenCraftingRecipeTest {

    @Test
    void optimizePattern() {
        Assertions.assertArrayEquals(new String[]{"  ", "  "},
                GenCraftingRecipe.optimizePattern(new String[]{"   ", "   ", "   "}));

        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                GenCraftingRecipe.optimizePattern(new String[]{"   ", " AA", " AA"}));
        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                GenCraftingRecipe.optimizePattern(new String[]{"   ", "AA ", "AA "}));
        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                GenCraftingRecipe.optimizePattern(new String[]{"AA ", "AA ", "   "}));
        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                GenCraftingRecipe.optimizePattern(new String[]{" AA", " AA", "   "}));

        Assertions.assertArrayEquals(new String[]{"   ", "AAA", "AAA"},
                GenCraftingRecipe.optimizePattern(new String[]{"   ", "AAA", "AAA"}));
        Assertions.assertArrayEquals(new String[]{"AAA", "AAA", "   "},
                GenCraftingRecipe.optimizePattern(new String[]{"AAA", "AAA", "   "}));
    }
}