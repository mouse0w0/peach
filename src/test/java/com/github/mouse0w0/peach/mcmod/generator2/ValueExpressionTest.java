package com.github.mouse0w0.peach.mcmod.generator2;

import freemarker.template.Configuration;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValueExpressionTest {
    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_34);

    @Test
    public void eval() {
        assertEquals(true, new ValueExpression("a == 'b'", CONFIGURATION).eval(Map.of("a", "b")));
        assertEquals(false, new ValueExpression("a == 'b'", CONFIGURATION).eval(Map.of("a", "c")));
        assertEquals(BigDecimal.valueOf(3), new ValueExpression("1 + 2", CONFIGURATION).eval(null));
        assertEquals("Hello World", new ValueExpression("a + ' ' + b", CONFIGURATION).eval(Map.of("a", "Hello", "b", "World")));
    }
}