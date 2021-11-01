package com.u1tramarinet.imagemodifier.model.color;

import com.u1tramarinet.imagemodifier.model.analysis.Point;

/**
 * 0.0-1.0
 */
public class XyzColor extends Point {
    public final double alpha;
    public final double x;
    public final double y;
    public final double z;

    public XyzColor(double alpha, double x, double y, double z) {
        super(x, y, z);
        this.alpha = alpha;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
