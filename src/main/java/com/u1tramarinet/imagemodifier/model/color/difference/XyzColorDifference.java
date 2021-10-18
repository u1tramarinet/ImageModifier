package com.u1tramarinet.imagemodifier.model.color.difference;

import com.u1tramarinet.imagemodifier.model.color.ColorTranslator;
import com.u1tramarinet.imagemodifier.model.color.RgbColor;
import com.u1tramarinet.imagemodifier.model.color.XyzColor;

public class XyzColorDifference implements ColorDifference {

    private final static RgbColor WHITE = new RgbColor(255, 255, 255, 255);
    private final static RgbColor BLACK = new RgbColor(255, 0, 0, 0);
    private final double MAX;

    public XyzColorDifference() {
        MAX = compare(WHITE, BLACK);
    }

    @Override
    public double compare(RgbColor one, RgbColor another) {
        return compare(
                ColorTranslator.translateToXyz(one),
                ColorTranslator.translateToXyz(another));
    }

    private double compare(XyzColor one, XyzColor another) {
        double xDiff = one.x - another.x;
        double yDiff = one.y - another.y;
        double zDiff = one.z - another.z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff) / MAX;
    }
}
