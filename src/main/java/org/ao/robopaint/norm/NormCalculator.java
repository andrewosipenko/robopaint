package org.ao.robopaint.norm;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

public interface NormCalculator {
    double calculate(LineImage lineImage);
    double calculateNorm(Line previousLine, boolean previousReverse, Line line, boolean reverse);
}
