package com.u1tramarinet.imagemodifier.model.analysis;

public class Pixel extends Point {
    public final int x;
    public final int y;

    public Pixel(int x, int y, double... coordinates) {
        super(coordinates);
        this.x = x;
        this.y = y;
    }

    public Pixel(int x, int y, Pixel pixel) {
        this(x, y, (Point) pixel);
    }

    public Pixel(int x, int y, Point point) {
        super(point);
        this.x = x;
        this.y = y;
    }
}
