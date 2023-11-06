package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public final class MinecraftVersion implements Comparable<MinecraftVersion> {
    public static final MinecraftVersion V1_21 = of(1, 21);
    public static final MinecraftVersion V1_20 = of(1, 20);
    public static final MinecraftVersion V1_19 = of(1, 19);
    public static final MinecraftVersion V1_18 = of(1, 18);
    public static final MinecraftVersion V1_17 = of(1, 17);
    public static final MinecraftVersion V1_16 = of(1, 16);
    public static final MinecraftVersion V1_15 = of(1, 15);
    public static final MinecraftVersion V1_14 = of(1, 14);
    public static final MinecraftVersion V1_13 = of(1, 13);
    public static final MinecraftVersion V1_12 = of(1, 12);
    public static final MinecraftVersion V1_11 = of(1, 11);
    public static final MinecraftVersion V1_10 = of(1, 10);
    public static final MinecraftVersion V1_9 = of(1, 9);
    public static final MinecraftVersion V1_8 = of(1, 8);
    public static final MinecraftVersion V1_7 = of(1, 7);
    public static final MinecraftVersion V1_6 = of(1, 6);
    public static final MinecraftVersion V1_5 = of(1, 5);
    public static final MinecraftVersion V1_4 = of(1, 4);
    public static final MinecraftVersion V1_3 = of(1, 3);
    public static final MinecraftVersion V1_2 = of(1, 2);
    public static final MinecraftVersion V1_1 = of(1, 1);
    public static final MinecraftVersion V1_0 = of(1, 0);

    private final int major;
    private final int minor;
    private final int patch;

    public static MinecraftVersion of(String version) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        String[] split = StringUtils.split(version, '.');
        return switch (split.length) {
            case 2 -> of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), 0);
            case 3 -> of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            default -> throw new IllegalArgumentException("Invalid minecraft version: " + version);
        };
    }

    public static MinecraftVersion of(int major, int minor) {
        return new MinecraftVersion(major, minor, 0);
    }

    public static MinecraftVersion of(int major, int minor, int patch) {
        return new MinecraftVersion(major, minor, patch);
    }

    private MinecraftVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public int compareTo(@NotNull MinecraftVersion o) {
        if (major != o.major) return major > o.major ? 1 : -1;
        if (minor != o.minor) return minor > o.minor ? 1 : -1;
        if (patch != o.patch) return patch > o.patch ? 1 : -1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MinecraftVersion that = (MinecraftVersion) o;

        return major == that.major && minor == that.minor && patch == that.patch;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        return result;
    }

    @Override
    public String toString() {
        return patch == 0 ? major + "." + minor : major + "." + minor + "." + patch;
    }
}
