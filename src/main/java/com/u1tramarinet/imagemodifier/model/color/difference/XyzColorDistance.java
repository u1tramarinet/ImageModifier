package com.u1tramarinet.imagemodifier.model.color.difference;

import com.u1tramarinet.imagemodifier.model.analysis.Distance;
import com.u1tramarinet.imagemodifier.model.color.ColorTranslator;
import com.u1tramarinet.imagemodifier.model.color.RgbColor;
import com.u1tramarinet.imagemodifier.model.color.XyzColor;

public class XyzColorDistance extends Distance<XyzColor> {

    private final static RgbColor WHITE = new RgbColor(255, 255, 255, 255);
    private final static RgbColor BLACK = new RgbColor(255, 0, 0, 0);
    private final double MAX;

    public XyzColorDistance() {
        MAX = compare(ColorTranslator.translateToXyz(WHITE), ColorTranslator.translateToXyz(BLACK));
    }

    @Override
    public double get(XyzColor one, XyzColor another) {
        return compare(one, another);
    }

    private double compare(XyzColor one, XyzColor another) {
        double xDiff = one.x - another.x;
        double yDiff = one.y - another.y;
        double zDiff = one.z - another.z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff) / MAX;
    }
}
