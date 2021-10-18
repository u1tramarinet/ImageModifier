package com.u1tramarinet.imagemodifier.model.color.difference;

import com.u1tramarinet.imagemodifier.model.color.ColorTranslator;
import com.u1tramarinet.imagemodifier.model.color.LabColor;
import com.u1tramarinet.imagemodifier.model.color.RgbColor;

public class LabColorDifference implements ColorDifference {

    private final static RgbColor WHITE = new RgbColor(255, 255, 255, 255);
    private final static RgbColor BLACK = new RgbColor(255, 0, 0, 0);
    private final double MAX;

    public LabColorDifference() {
        MAX = compare(WHITE, BLACK);
    }

    @Override
    public double compare(RgbColor one, RgbColor another) {
        return compare(
                ColorTranslator.translateToLab(one),
                ColorTranslator.translateToLab(another));
    }

    private double compare(LabColor one, LabColor another) {
        double xDiff = one.x - another.x;
        double yDiff = one.y - another.y;
        double zDiff = one.z - another.z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff) / MAX;
    }
}
