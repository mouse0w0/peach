package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CraftingRecipeGenTest {

    @Test
    void optimizePattern() {
        Assertions.assertArrayEquals(new String[]{"  ", "  "},
                CraftingRecipeGen.optimizePattern(new String[]{"   ", "   ", "   "}));

        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                CraftingRecipeGen.optimizePattern(new String[]{"   ", " AA", " AA"}));
        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                CraftingRecipeGen.optimizePattern(new String[]{"   ", "AA ", "AA "}));
        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                CraftingRecipeGen.optimizePattern(new String[]{"AA ", "AA ", "   "}));
        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                CraftingRecipeGen.optimizePattern(new String[]{" AA", " AA", "   "}));

        Assertions.assertArrayEquals(new String[]{"   ", "AAA", "AAA"},
                CraftingRecipeGen.optimizePattern(new String[]{"   ", "AAA", "AAA"}));
        Assertions.assertArrayEquals(new String[]{"AAA", "AAA", "   "},
                CraftingRecipeGen.optimizePattern(new String[]{"AAA", "AAA", "   "}));
    }
}