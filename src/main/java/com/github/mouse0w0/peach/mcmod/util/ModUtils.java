package com.github.mouse0w0.peach.mcmod.util;

import com.github.promeg.pinyinhelper.Pinyin;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class ModUtils {

    public static final Pattern IDENTIFIER = Pattern.compile("[a-z][a-z0-9_]{0,63}");

    public static boolean isValidModId(String modId) {
        return IDENTIFIER.matcher(modId).matches();
    }

    public static boolean isValidRegisterName(String name) {
        return IDENTIFIER.matcher(name).matches();
    }

    @Nullable
    public static String toRegisterName(String name) {
        String registerName = tryConvertToRegisterName(name);
        return isValidRegisterName(registerName) ? registerName : null;
    }

    public static String tryConvertToRegisterName(String name) {
        StringBuilder sb = new StringBuilder(name.length());
        for (char c : name.toCharArray()) {
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
