package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.norm.NormCalculator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractAreaTransformer implements LineImageTransformer {
    protected final double ratio;
    private final int minAreaSize;
    protected final NormCalculator normCalculator;
    private boolean gaussian;

    public AbstractAreaTransformer(double ratio, int minAreaSize, NormCalculator normCalculator, boolean gaussian) {
        this.ratio = ratio;
        this.minAreaSize = minAreaSize;
        this.normCalculator = normCalculator;
        this.gaussian = gaussian;
    }

    protected int getArea(LineImage source) {
        int maxAreaSize = (int)(ratio * (source.lines.length - minAreaSize));
        if (maxAreaSize < minAreaSize){
            return minAreaSize;
        }
        return nextArea(maxAreaSize);
    }

    private int nextArea(int maxAreaSize) {
        Random random = ThreadLocalRandom.current();
        int result;
        if(gaussian && random.nextBoolean()){
            result = random.nextInt(10) + 1;
        }
        else {
            result = random.nextInt(maxAreaSize) + 1;
        }
        if (result < 1) {
            return 1;
        }
        return result;
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
