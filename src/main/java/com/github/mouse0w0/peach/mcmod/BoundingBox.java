package com.github.mouse0w0.peach.mcmod;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BoundingBox {
    public static final NumberFormat NUMBER_FORMAT = new DecimalFormat("0.000E0;-");

    private final double minX;
    private final double minY;
    private final double minZ;
    private final double maxX;
    private final double maxY;
    private final double maxZ;

    public BoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public double minX() {
        return minX;
    }

    public double minY() {
        return minY;
    }

    public double minZ() {
        return minZ;
    }

    public double maxX() {
        return maxX;
    }

    public double maxY() {
        return maxY;
    }

    public double maxZ() {
        return maxZ;
    }

    public double lengthX() {
        return maxX - minX;
    }

    public double lengthY() {
        return maxY - minY;
    }

    public double lengthZ() {
        return maxZ - minZ;
    }

    public boolean contains(BoundingBox other) {
        return other.minX >= minX && other.maxX <= maxX &&
                other.minY >= minY && other.maxY <= maxY &&
                other.minZ >= minZ && other.maxZ <= maxZ;
    }

    public boolean intersects(BoundingBox other) {
        return maxX > other.minX && maxY > other.minY && maxZ > other.minZ &&
                minX < other.maxX && minY < other.maxY && minZ < other.maxZ;
    }

    public BoundingBox union(BoundingBox other) {
        return new BoundingBox(
                Math.min(minX, other.minX),
                Math.min(minY, other.minY),
                Math.min(minZ, other.minZ),
                Math.max(maxX, other.maxX),
                Math.max(maxY, other.maxY),
                Math.max(maxZ, other.maxZ));
    }

    public BoundingBox intersection(BoundingBox other) {
        return new BoundingBox(
                Math.max(minX, other.minX),
                Math.max(minY, other.minY),
                Math.max(minZ, other.minZ),
                Math.min(maxX, other.maxX),
                Math.min(maxY, other.maxY),
                Math.min(maxZ, other.maxZ));
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(minX);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoundingBox that = (BoundingBox) o;

        if (Double.compare(that.minX, minX) != 0) return false;
        if (Double.compare(that.minY, minY) != 0) return false;
        if (Double.compare(that.minZ, minZ) != 0) return false;
        if (Double.compare(that.maxX, maxX) != 0) return false;
        if (Double.compare(that.maxY, maxY) != 0) return false;
        return Double.compare(that.maxZ, maxZ) == 0;
    }

    @Override
    public String toString() {
        return formatNumbers(toString(NUMBER_FORMAT));
    }

    public String toString(NumberFormat format) {
        return "(" + format(minX, format) + "  " + format(minY, format) + "  " + format(minZ, format) + ") < " +
                "(" + format(maxX, format) + "  " + format(maxY, format) + "  " + format(maxZ, format) + ")";
    }

    private static String format(double number, NumberFormat format) {
        if (Double.isNaN(number)) {
            return "NaN";
        } else if (Double.isInfinite(number)) {
            return number > 0.0 ? "+Inf" : "-Inf";
        } else {
            return format.format(number);
        }
    }

    private static String formatNumbers(String str) {
        StringBuilder sb = new StringBuilder();
        int eIndex = Integer.MAX_VALUE;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == 'E') {
                eIndex = i;
            } else if (Character.isDigit(c) && eIndex == i - 1) {
                sb.append('+');
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
