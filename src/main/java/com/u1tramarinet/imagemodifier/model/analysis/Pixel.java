package com.u1tramarinet.imagemodifier.model.analysis;

public class Pixel<P extends Point> {
    public final int x;
    public final int y;
    public P point;

    public Pixel(int x, int y, Pixel<P> pixel) {
        this(x, y, pixel.point);
    }

    public Pixel(int x, int y, P point) {
        this.x = x;
        this.y = y;
        this.point = point;
    }
}
