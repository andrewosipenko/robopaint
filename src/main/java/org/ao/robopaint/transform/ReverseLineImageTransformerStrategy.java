package org.ao.robopaint.transform;

import org.ao.robopaint.norm.NormCalculator;
import org.ao.robopaint.norm.NormedLineImage;

import java.util.concurrent.ThreadLocalRandom;

public class ReverseLineImageTransformerStrategy implements LineImageTransformerStrategy<NormedLineImage> {
    private final double ratio;

    public ReverseLineImageTransformerStrategy(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public void transform(NormedLineImage source, NormedLineImage target) {
        int area = ThreadLocalRandom.current().nextInt((int)(ratio * (source.lines.length - 2))) + 1;
        int start = ThreadLocalRandom.current().nextInt(source.lines.length - area - 1);

        target.clone(source);
        for(int i = start; i < start + area; i++) {
            target.reverse[i] = !target.reverse[i];
        }
    }
}
