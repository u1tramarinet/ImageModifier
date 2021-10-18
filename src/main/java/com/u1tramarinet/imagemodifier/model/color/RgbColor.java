package com.u1tramarinet.imagemodifier.model.color;

/**
 * 0-255
 */
public class RgbColor {
    public final int alpha;
    public final int red;
    public final int green;
    public final int blue;
    public final int intValue;
    public final String hexString;

    public RgbColor(int alpha, int red, int green, int blue) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
        intValue = toIntValue(alpha, red, green, blue);
        hexString = toHexString(alpha, red, green, blue);
    }

    public RgbColor(int intValue) {
        this.intValue = intValue;
        alpha = toAlpha(intValue);
        red = toRed(intValue);
        green = toGreen(intValue);
        blue = toBlue(intValue);
        hexString = toHexString(alpha, red, green, blue);
    }

    private String toHexString(int alpha, int red, int green, int blue) {
        return String.format("%2X", alpha) +
                String.format("%2X", red) +
                String.format("%2X", green) +
                String.format("%2X", blue);
    }

    private int toIntValue(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private int toAlpha(int argb) {
        return ((0xff000000 & argb) >> 24);
    }

    private int toRed(int argb) {
        return ((0xff0000 & argb) >> 16);
    }

    private int toGreen(int argb) {
        return ((0xff00 & argb) >> 8);
    }

    private int toBlue(int argb) {
        return (0xff & argb);
    }

    @Override
    public String toString() {
        return "Color{" +
                "alpha=" + alpha +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", intValue=" + intValue +
                ", hexString='" + hexString + '\'' +
                '}';
    }
}
