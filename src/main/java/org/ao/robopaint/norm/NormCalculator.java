package org.ao.robopaint.norm;

import org.ao.robopaint.image.LineImage;

@FunctionalInterface
public interface NormCalculator {
    double calculate(LineImage lineImage);
}
