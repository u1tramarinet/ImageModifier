package com.u1tramarinet.imagemodifier.model.analysis;

import com.u1tramarinet.imagemodifier.model.color.RgbColor;

public class Pixel {
    public final int x;
    public final int y;
    public final RgbColor rgbColor;

    public Pixel(int x, int y, RgbColor color) {
        this.x = x;
        this.y = y;
        this.rgbColor = color;
    }
}
