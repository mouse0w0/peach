package com.github.mouse0w0.peach.mcmod.util;

import com.anyascii.AnyAscii;

import java.util.regex.Pattern;

public class IdentifierUtils {
    public static final Pattern IDENTIFIER = Pattern.compile("^[a-z0-9_]+$");

    public static boolean validateIdentifier(String identifier) {
        return identifier != null && IDENTIFIER.matcher(identifier).matches();
    }

    public static String toIdentifier(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            String trans = AnyAscii.transliterate(s.codePointAt(i));
            for (int j = 0; j < trans.length(); j++) {
                char transChar = trans.charAt(j);
                if (isIdentifierPart(transChar)) {
                    sb.append(transChar);
                } else if (Character.isUpperCase(transChar)) {
                    sb.append(Character.toLowerCase(transChar));
                } else {
                    sb.append('_');
                }
            }
        }
        return sb.toString();
    }

    private static boolean isIdentifierPart(char c) {
        return c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '_';
    }
}
