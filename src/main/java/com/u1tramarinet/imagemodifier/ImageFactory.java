package com.u1tramarinet.imagemodifier;

import javafx.scene.image.*;

import java.nio.IntBuffer;

public class ImageFactory {
    WritableImage createWritableImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        return createWritableImage(width, height, image.getPixelReader());
    }

    WritableImage copyWritableImage(WritableImage image) {
        return createWritableImage((int) image.getWidth(), (int) image.getHeight(), image.getPixelReader());
    }

    WritableImage createWritableImage(int width, int height, PixelReader reader) {
        WritableImage writableImage = new WritableImage(width, height);

        PixelWriter writer = writableImage.getPixelWriter();

        WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
        int[] pixels = new int[width * height];
        reader.getPixels(0, 0, width, height, format, pixels, 0, width);
        writer.setPixels(0, 0, width, height, format, pixels, 0, width);

        return writableImage;
    }
}
