package com.github.mouse0w0.peach.mcmod.util;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.regex.Pattern;

public class JavaUtils {

    public static final Set<String> RESERVED_WORDS = Sets.newHashSet(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "extends", "false", "final", "finally", "float",
            "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
            "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void",
            "volatile", "while");

    public static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9_$]*");

    public static boolean isJavaIdentifier(String identifier) {
        if (RESERVED_WORDS.contains(identifier)) return false;

        return IDENTIFIER_PATTERN.matcher(identifier).matches();
    }

    public static String lowerUnderscoreToUpperCamel(String s) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s);
    }

    public static String lowerUnderscoreToLowerCamel(String s) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
    }

    public static String lowerUnderscoreToUpperUnderscore(String s) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, s);
    }
}
