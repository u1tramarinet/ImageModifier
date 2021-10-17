package com.u1tramarinet.imagemodifier;

public class PixelInfo {
    int x;
    int y;
    Argb color;

    PixelInfo(int x, int y, Argb color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PixelInfo pixelInfo = (PixelInfo) o;

        if (x != pixelInfo.x) return false;
        return y == pixelInfo.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
