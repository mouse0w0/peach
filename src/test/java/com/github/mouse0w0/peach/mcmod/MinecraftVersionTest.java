package com.github.mouse0w0.peach.mcmod;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinecraftVersionTest {

    @Test
    void test() {
        MinecraftVersion V1_12 = MinecraftVersion.of("1.12");
        MinecraftVersion V1_13 = MinecraftVersion.of("1.13");
        MinecraftVersion V1_12_2 = MinecraftVersion.of("1.12.2");
        assertEquals("1.12", V1_12.toString());
        assertEquals("1.13", V1_13.toString());
        assertEquals("1.12.2", V1_12_2.toString());
        assertNotEquals(V1_12, V1_12_2);
        assertNotEquals(V1_13, V1_12_2);
        assertTrue(V1_12.compareTo(V1_12_2) < 0);
        assertTrue(V1_13.compareTo(V1_12_2) > 0);
    }
}