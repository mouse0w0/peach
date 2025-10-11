package com.github.mouse0w0.peach.mcmod.generator2;

import freemarker.template.Configuration;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringExpressionTest {

    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_34);

    @Test
    public void eval() {
        assertEquals("a + ' ' + b", new StringExpression("a + ' ' + b", CONFIGURATION).eval(Map.of("a", "Hello", "b", "World")));
        assertEquals("Hello World", new StringExpression("${a} ${b}", CONFIGURATION).eval(Map.of("a", "Hello", "b", "World")));
        assertEquals("Hello World", new StringExpression("${a + ' ' + b}", CONFIGURATION).eval(Map.of("a", "Hello", "b", "World")));
    }
}