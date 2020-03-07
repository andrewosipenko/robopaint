package org.ao.robopaint.transform;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.norm.NormedLineImage;

import java.util.concurrent.ThreadLocalRandom;

public class ReverseLineImageTransformerStrategy implements LineImageTransformerStrategy<NormedLineImage> {
    private final double ratio;

    public ReverseLineImageTransformerStrategy(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public void transform(NormedLineImage source, NormedLineImage target) {
        int area = ThreadLocalRandom.current().nextInt((int)(ratio * (source.lines.length - 1)) + 1) + 1;
        int start = ThreadLocalRandom.current().nextInt(source.lines.length - area);

        target.clone(source);
        int end = start + area;
        if(area > 1) {
            for (int i = 0; i <= area / 2; i++) {
                Line line = target.lines[start + i];
                target.lines[start + i] = target.lines[end - i];
                target.lines[end - i] = line;

                boolean reverse = target.reverse[start + i];
                target.reverse[start + i] = !target.reverse[end - i];
                target.reverse[end - i] = reverse;
            }
        }
        if(area % 2 == 1){
            int i = start + area / 2 + 1;
            target.reverse[i] = !target.reverse[i];
        }
    }
}
