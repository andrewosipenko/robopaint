package org.ao.robopaint.transform.indexed;

import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.norm.NormCalculator;
import org.ao.robopaint.transform.LineImageTransformer;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractAreaTransformer implements LineImageTransformer {
    protected final double ratio;
    private final int minAreaSize;
    protected final NormCalculator normCalculator;

    public AbstractAreaTransformer(double ratio, int minAreaSize, NormCalculator normCalculator) {
        this.ratio = ratio;
        this.minAreaSize = minAreaSize;
        this.normCalculator = normCalculator;
    }

    protected int getArea(LineImage source) {
        Random random = ThreadLocalRandom.current();
        int maxAreaSize = (int)(ratio * (source.lines.length - minAreaSize));
        if (maxAreaSize < minAreaSize){
            return minAreaSize;
        }
        return random.nextInt(maxAreaSize) + 1;
    }

    protected int getStart(LineImage source, int area) {
        int maxStart = source.lines.length - area;
        if (maxStart == 0) {
            return 0;
        }
        Random random = ThreadLocalRandom.current();
        return random.nextInt(maxStart + 1);
    }
}
