package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CraftingRecipeGeneratorTest {

    @Test
    void optimizePattern() {
        Assertions.assertArrayEquals(new String[]{"  ", "  "},
                CraftingRecipeGenerator.optimizePattern(new String[]{"   ", "   ", "   "}));

        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                CraftingRecipeGenerator.optimizePattern(new String[]{"   ", " AA", " AA"}));
        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                CraftingRecipeGenerator.optimizePattern(new String[]{"   ", "AA ", "AA "}));
        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                CraftingRecipeGenerator.optimizePattern(new String[]{"AA ", "AA ", "   "}));
        Assertions.assertArrayEquals(new String[]{"AA", "AA"},
                CraftingRecipeGenerator.optimizePattern(new String[]{" AA", " AA", "   "}));

        Assertions.assertArrayEquals(new String[]{"   ", "AAA", "AAA"},
                CraftingRecipeGenerator.optimizePattern(new String[]{"   ", "AAA", "AAA"}));
        Assertions.assertArrayEquals(new String[]{"AAA", "AAA", "   "},
                CraftingRecipeGenerator.optimizePattern(new String[]{"AAA", "AAA", "   "}));
    }
}