package com.github.mouse0w0.peach.forge.util;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.regex.Pattern;

public class ModUtils {

    public static final Pattern REGISTER_NAME = Pattern.compile("[a-z][a-z0-9_]*");
    public static final Pattern CLASS_NAME = Pattern.compile("[a-zA-Z][a-zA-Z0-9_$]*");

    public static String toRegisterName(String name) {
        StringBuilder sb = new StringBuilder(name.length());
        for (char c : name.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            } else if (Pinyin.isChinese(c)) {
                if (sb.length() != 0) sb.append("_");
                sb.append(Pinyin.toPinyin(c).toLowerCase());
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
