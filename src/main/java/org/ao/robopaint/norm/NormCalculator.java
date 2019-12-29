package org.ao.robopaint.norm;

import org.ao.robopaint.image.indexed.IndexedLineImage;

public interface NormCalculator {
    double calculate(IndexedLineImage lineImage);
    double calculateNorm(int start, int end);
}
