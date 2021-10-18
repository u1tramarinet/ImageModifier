package com.u1tramarinet.imagemodifier.model.color.difference;

import com.u1tramarinet.imagemodifier.model.color.RgbColor;

public interface ColorDifference {
    double compare(RgbColor one, RgbColor another);
}
