package com.u1tramarinet.imagemodifier.model.color.difference;

import com.u1tramarinet.imagemodifier.model.analysis.Distance;
import com.u1tramarinet.imagemodifier.model.color.ColorTranslator;
import com.u1tramarinet.imagemodifier.model.color.LabColor;
import com.u1tramarinet.imagemodifier.model.color.RgbColor;

public class LabColorDistance extends Distance<LabColor> {

    private final static RgbColor WHITE = new RgbColor(255, 255, 255, 255);
    private final static RgbColor BLACK = new RgbColor(255, 0, 0, 0);
    private final double MAX;

    public LabColorDistance() {
        MAX = compare(ColorTranslator.translateToLab(WHITE),
                ColorTranslator.translateToLab(BLACK));
    }

    @Override
    public double get(LabColor one, LabColor another) {
        return compare(one, another);
    }

    private double compare(LabColor one, LabColor another) {
        double xDiff = one.x - another.x;
        double yDiff = one.y - another.y;
        double zDiff = one.z - another.z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff) / MAX;
    }
}
