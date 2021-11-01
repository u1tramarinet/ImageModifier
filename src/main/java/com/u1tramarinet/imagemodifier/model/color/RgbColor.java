package com.u1tramarinet.imagemodifier.model.color;

import com.u1tramarinet.imagemodifier.model.analysis.Point;

public class RgbColor extends Point {
    public final int alpha;
    public final int red;
    public final int green;
    public final int blue;

    public RgbColor(int alpha, int red, int green, int blue) {
        super(red, green, blue);
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}
