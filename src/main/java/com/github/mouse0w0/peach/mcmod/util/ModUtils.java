package com.github.mouse0w0.peach.mcmod.util;

import com.github.promeg.pinyinhelper.Pinyin;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public final class ModUtils {

    public static final Pattern IDENTIFIER = Pattern.compile("[a-z][a-z0-9_]{0,63}");

    public static boolean isValidIdentifier(String identifier) {
        return identifier != null && IDENTIFIER.matcher(identifier).matches();
    }

    @Nullable
    public static String toIdentifier(String s) {
        String identifier = tryConvertToIdentifier(s);
        return isValidIdentifier(identifier) ? identifier : null;
    }

    public static String tryConvertToIdentifier(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (char c : s.toCharArray()) {
            if (isASCII(c)) {
                if (Character.isWhitespace(c)) {
                    sb.append("_");
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            } else if (Pinyin.isChinese(c)) {
                if (sb.length() != 0) sb.append("_");
                sb.append(Pinyin.toPinyin(c).toLowerCase());
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
