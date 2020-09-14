package com.github.mouse0w0.peach.mcmod.util;

import com.github.promeg.pinyinhelper.Pinyin;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class ModUtils {

    public static final Pattern REGISTER_NAME = Pattern.compile("[a-z][a-z0-9_]*");

    public static boolean validRegisterName(String name) {
        return REGISTER_NAME.matcher(name).matches();
    }

    public static String toRegisterName(String name) {
        StringBuilder sb = new StringBuilder(name.length());
        for (char c : name.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            } else if (Pinyin.isChinese(c)) {
                if (sb.length() != 0) sb.append("_");
                sb.append(Pinyin.toPinyin(c).toLowerCase());
            } else if (c == ' ' || c == '\t') {
                sb.append("_");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Nullable
    public static String forceRegisterName(String name) {
        if (validRegisterName(name)) return name;
        String registerName = toRegisterName(name);
        return validRegisterName(registerName) ? registerName : null;
    }
}
