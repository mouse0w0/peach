package com.github.mouse0w0.peach.mcmod.util;

import gcardone.junidecode.Junidecode;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public final class ModUtils {

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
        s = Junidecode.unidecode(s).trim();
        StringBuilder sb = new StringBuilder(s.length());
        for (char c : s.toCharArray()) {
            if (isASCII(c)) {
                if (Character.isWhitespace(c)) {
                    sb.append("_");
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            } else {
                sb.append(Integer.toHexString(c));
            }
        }
        return sb.toString();
    }

    private static boolean isASCII(char c) {
        return c <= 127;
    }
}
