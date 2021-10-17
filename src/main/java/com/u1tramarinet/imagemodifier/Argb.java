package com.u1tramarinet.imagemodifier;

public class Argb {
    int a;
    int r;
    int g;
    int b;
    int color;

    Argb(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        color = (a << 24) | (r << 16) | (g << 8) | b;
    }

    Argb(int color) {
        a = color >> 24;
        r = (color >> 16) & 0xFF;
        g = (color >> 8) & 0xFF;
        b = color & 0xFF;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Argb{" +
                "a=" + a +
                ", r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}
