package com.github.mouse0w0.peach.mcmod.util;

import com.anyascii.AnyAscii;

import java.util.regex.Pattern;

public class ModIdUtils {
    public static final Pattern MODID = Pattern.compile("^[a-z][a-z0-9_]{1,63}$");

    public static boolean validateModId(String modId) {
        return modId != null && MODID.matcher(modId).matches();
    }

    public static String toModId(String s) {
        StringBuilder sb = new StringBuilder();
        boolean prevIsUnderline = false;
        for (int i = 0; i < s.length(); i++) {
            String trans = AnyAscii.transliterate(s.codePointAt(i));
            for (int j = 0; j < trans.length(); j++) {
                char transChar = trans.charAt(j);
                if (isModIdPart(transChar)) {
                    sb.append(transChar);
                    prevIsUnderline = false;
                } else if (Character.isUpperCase(transChar)) {
                    sb.append(Character.toLowerCase(transChar));
                    prevIsUnderline = false;
                } else if (!prevIsUnderline) {
                    sb.append('_');
                    prevIsUnderline = true;
                }
            }
        }
        return sb.toString();
    }

    private static boolean isModIdPart(char c) {
        return c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '_';
    }
}
