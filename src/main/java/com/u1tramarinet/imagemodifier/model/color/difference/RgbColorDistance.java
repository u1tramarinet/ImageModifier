package com.u1tramarinet.imagemodifier.model.color.difference;

import com.u1tramarinet.imagemodifier.model.analysis.Distance;
import com.u1tramarinet.imagemodifier.model.color.RgbColor;

public class RgbColorDistance extends Distance<RgbColor> {

    @Override
    public double get(RgbColor one, RgbColor another) {
        double rDiff = one.red - another.red;
        double gDiff = one.green - another.green;
        double bDiff = one.blue - another.blue;
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff) / Math.sqrt(3);
    }
}