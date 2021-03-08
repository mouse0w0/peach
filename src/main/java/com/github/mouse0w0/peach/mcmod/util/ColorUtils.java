package com.github.mouse0w0.peach.mcmod.util;

import javafx.scene.paint.Color;
import org.joml.Vector4d;
import org.joml.Vector4dc;

public class ColorUtils {
    public static Color toColor(Vector4dc v) {
        return new Color(v.x(), v.y(), v.z(), v.w());
    }

    public static Vector4d fromColor(Color color) {
        return new Vector4d(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }
}
