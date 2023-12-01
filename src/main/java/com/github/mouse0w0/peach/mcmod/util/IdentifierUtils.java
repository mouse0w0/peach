package com.github.mouse0w0.peach.mcmod.util;

import com.anyascii.AnyAscii;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class IdentifierUtils {
    public static final Pattern IDENTIFIER = Pattern.compile("^[a-z][a-z0-9_]{1,63}$");

    public static boolean validateIdentifier(String identifier) {
        return identifier != null && IDENTIFIER.matcher(identifier).matches();
    }

    @Nullable
    public static String toIdentifier(String s) {
        String identifier = tryConvertToIdentifier(s);
        return validateIdentifier(identifier) ? identifier : null;
    }

    public static String tryConvertToIdentifier(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            String trans = AnyAscii.transliterate(s.codePointAt(i));
            for (int j = 0; j < trans.length(); j++) {
                char transChar = trans.charAt(j);
                if (isIdentifierPart(transChar)) {
                    sb.append(transChar);
                } else if (isUppercaseLetter(transChar)) {
                    sb.append((char) (transChar - 'A' + 'a'));
                } else if (Character.isWhitespace(transChar)) {
                    sb.append('_');
                }
            }
        }
        return sb.toString();
    }

    public static boolean isIdentifierStart(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static boolean isIdentifierPart(char c) {
        return c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '_';
    }

    private static boolean isUppercaseLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }
}
