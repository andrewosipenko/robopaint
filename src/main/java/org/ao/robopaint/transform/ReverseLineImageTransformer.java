package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.norm.NormCalculator;

import java.util.concurrent.ThreadLocalRandom;

public class ReverseLineImageTransformer implements LineImageTransformer {
    private final double ratio;
    private final NormCalculator normCalculator;

    public ReverseLineImageTransformer(double ratio, NormCalculator normCalculator) {
        this.ratio = ratio;
        this.normCalculator = normCalculator;
    }

    @Override
    public LineImage transform(LineImage source) {
        if(source.lines[0] == source.lines[1]){
            throw new IllegalStateException();
        }
        int areaBound = Math.max(1, (int)(ratio * (source.lines.length - 2)));
        int area = ThreadLocalRandom.current().nextInt(areaBound) + 1;
        int start = ThreadLocalRandom.current().nextInt(source.lines.length - area + 1);

        LineImage target = new LineImage(source);
        int end = start + area - 1;
        for(int i = 0; i <= end - start; i++) {
            target.reverse[i + start] = !target.reverse[i + start];
            target.lines[i + start] = source.lines[end - i];
        }

        target.setNorm(normCalculator.calculate(target));
        if(target.lines[0] == target.lines[1]){
            throw new IllegalStateException();
        }
        return target;
    }
}
