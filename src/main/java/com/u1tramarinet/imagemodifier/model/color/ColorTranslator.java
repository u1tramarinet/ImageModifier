package com.u1tramarinet.imagemodifier.model.color;

import java.util.function.Function;

public class ColorTranslator {

    private static final double[][] FORMULA_RGB_TO_XYZ;
    private static final double[][] FORMULA_XYZ_TO_RGB;

    private static final XyzColor WHITE = new XyzColor(255, 255, 255, 255);
    private static final double D13 = 1d / 3;
    private static final double D629 = 6d / 29;
    private static final double D296 = 29d / 6;
    private static final double D429 = 4d / 29;
    private static final double D16116 = 16d / 116;
    private static final double T1 = Math.pow(D629, 3);

    static {
        /* http://www.enjoy.ne.jp/~k-ichikawa/CIEXYZ_RGB.html */
        FORMULA_RGB_TO_XYZ = new double[][]{
                {0.4124, 0.3576, 0.1805},/* X */
                {0.2126, 0.7152, 0.0722},/* Y */
                {0.0193, 0.1192, 0.9505} /* Z */
                /*    R,      G,      B */
        };
        FORMULA_XYZ_TO_RGB = new double[][]{
                {3.2410, -1.5374, -0.4986},/* R */
                {-0.9692, 1.8760, 0.0416},/* G */
                {0.0556, -0.2040, 1.0507} /* B */
                /*     X,       Y,       Z */
        };
    }

    public static XyzColor translateToXyz(RgbColor rgb) {
        double a = rgb.alpha / 255d;
        double r = rgb.red / 255d;
        double g = rgb.green / 255d;
        double b = rgb.blue / 255d;
        double x = FORMULA_RGB_TO_XYZ[0][0] * r + FORMULA_RGB_TO_XYZ[0][1] * g + FORMULA_RGB_TO_XYZ[0][2] * b;
        double y = FORMULA_RGB_TO_XYZ[1][0] * r + FORMULA_RGB_TO_XYZ[1][1] * g + FORMULA_RGB_TO_XYZ[1][2] * b;
        double z = FORMULA_RGB_TO_XYZ[2][0] * r + FORMULA_RGB_TO_XYZ[2][1] * g + FORMULA_RGB_TO_XYZ[2][2] * b;
        return new XyzColor(a, x, y, z);
    }

    public static RgbColor translateToRgb(XyzColor xyz) {
        int a = (int) xyz.alpha * 255;
        double r = FORMULA_XYZ_TO_RGB[0][0] * xyz.x + FORMULA_XYZ_TO_RGB[0][1] * xyz.y + FORMULA_XYZ_TO_RGB[0][2] * xyz.z;
        double g = FORMULA_XYZ_TO_RGB[1][0] * xyz.x + FORMULA_XYZ_TO_RGB[1][1] * xyz.y + FORMULA_XYZ_TO_RGB[1][2] * xyz.z;
        double b = FORMULA_XYZ_TO_RGB[2][0] * xyz.x + FORMULA_XYZ_TO_RGB[2][1] * xyz.y + FORMULA_XYZ_TO_RGB[2][2] * xyz.z;
        return new RgbColor(a, (int) r * 255, (int) g * 255, (int) b * 255);
    }

    public static LabColor translateToLab(RgbColor rgb) {
        return translateToLab(translateToXyz(rgb));
    }

    public static LabColor translateToLab(XyzColor xyz) {
        Function<Double, Double> function = in -> (in > T1) ? Math.pow(in, D13) : D13 * D296 * in + D429;
        double xx = function.apply(xyz.x / WHITE.x);
        double yy = function.apply(xyz.y / WHITE.y);
        double zz = function.apply(xyz.z / WHITE.z);
        double x = 116f * yy - 16;
        double y = 500 * (xx - yy);
        double z = 200 * (yy - zz);
        return new LabColor(xyz.alpha, x, y, z);
    }

    public static RgbColor translateToRgb(LabColor lab) {
        return translateToRgb(translateToXyz(lab));
    }

    public static XyzColor translateToXyz(LabColor lab) {
        double fy = (lab.x * 16) / 116;
        double fx = fy + lab.y / 500;
        double fz = fy + lab.z / 200;

        double x = (fx > D629) ? WHITE.x * fx * fx * fx : (fx - D16116) * 3 * D629 * D629 * WHITE.x;
        double y = (fy > D629) ? WHITE.y * fy * fy * fy : (fy - D16116) * 3 * D629 * D629 * WHITE.y;
        double z = (fx > D629) ? WHITE.z * fz * fz * fz : (fz - D16116) * 3 * D629 * D629 * WHITE.z;
        return new XyzColor(lab.alpha, x, y, z);
    }
}
