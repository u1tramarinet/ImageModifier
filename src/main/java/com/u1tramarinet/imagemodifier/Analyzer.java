package com.u1tramarinet.imagemodifier;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Analyzer {

    void modify(WritableImage image) {


    }

    void binarize(WritableImage src, WritableImage dst) {
        List<PixelInfo> pixels = setUpDataSet(src);
        float threshold = 0;
        for (PixelInfo pixelInfo : pixels) {
            Argb color = pixelInfo.color;
            threshold += (color.r + color.g + color.b) / 3f;
        }
        threshold /= pixels.size();

        PixelWriter writer = dst.getPixelWriter();
        for (PixelInfo pixelInfo : pixels) {
            Argb color = pixelInfo.color;
            float v = (color.r + color.g + color.b) / 3f;
            int modified = (v > threshold) ? 255 : 0;
            writer.setArgb(pixelInfo.x, pixelInfo.y, new Argb(color.a, modified, modified, modified).color);
        }
    }

    void monochrome(WritableImage src, WritableImage dst) {
        List<PixelInfo> pixels = setUpDataSet(src);

        PixelWriter writer = dst.getPixelWriter();
        for (PixelInfo pixelInfo : pixels) {
            Argb color = pixelInfo.color;
            int mono = (int) (0.299 * color.r + 0.587 * color.g + 0.114 * color.b);
            int argb = (color.a << 24) + (mono << 16) + (mono << 8) + mono;
            writer.setArgb(pixelInfo.x, pixelInfo.y, argb);
        }
    }

    void inverse(WritableImage src, WritableImage dst) {
        List<PixelInfo> pixels = setUpDataSet(src);

        PixelWriter writer = dst.getPixelWriter();
        for (PixelInfo pixelInfo : pixels) {
            Argb color = pixelInfo.color;
            writer.setArgb(pixelInfo.x, pixelInfo.y, new Argb(color.a, 255 - color.r, 255 - color.g, 255 - color.b).color);
        }
    }

    List<PixelInfo> findSimilarPixels(WritableImage src, PixelInfo basePixel, double threshold, List<PixelInfo> exceptFor) {
        List<PixelInfo> pixels = setUpDataSet(src);
        List<PixelInfo> result = new ArrayList<>();

        for (PixelInfo pixelInfo : pixels) {
            if ((pixelInfo.x == basePixel.x) && (pixelInfo.y == basePixel.y)) continue;
            if (exceptFor.contains(pixelInfo)) continue;
            double distance = getColorDistance(pixelInfo.color, basePixel.color);
            System.out.println("distance=" + distance + ", threshold=" + threshold);
            if (distance <= threshold) {
                result.add(pixelInfo);
            }
        }
        return result;
    }

    void highlightFindPixels(WritableImage dst, List<PixelInfo> pixels) {
        PixelWriter writer = dst.getPixelWriter();
        for (PixelInfo pixelInfo : pixels) {
            writer.setColor(pixelInfo.x, pixelInfo.y, Color.AQUA);
        }
    }

    private double getColorDistance(Argb one, Argb another) {
        double rDist = one.r - another.r;
        double gDist = one.g - another.g;
        double bDist = one.b - another.b;
        return Math.sqrt(rDist * rDist + gDist * gDist + bDist * bDist) / Math.sqrt(3);
    }

    private List<PixelInfo> setUpDataSet(WritableImage image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();
        List<PixelInfo> pixels = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Argb color = new Argb(reader.getArgb(x, y));
                if (color.a == 0) {
                    continue;
                }
                pixels.add(new PixelInfo(x, y, color));
            }
        }
        return pixels;
    }
}
