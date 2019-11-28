package org.ao.robopaint.transform;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SwapLineImageTransformerStrategy implements LineImageTransformerStrategy {
    private final double distanceRatio;

    public SwapLineImageTransformerStrategy(double distanceRatio) {
        this.distanceRatio = distanceRatio;
    }

    public SwapLineImageTransformerStrategy() {
        distanceRatio = 1;
    }

    @Override
    public void transform(LineImage source, LineImage target) {
        target.clone(source);

        Random random = ThreadLocalRandom.current();
        int i1 = random.nextInt(target.lines.length);
        int i2;
        if(distanceRatio == 1) {
            i2 = random.nextInt(target.lines.length);
        }
        else {
            i2 = random.nextInt((int)(target.lines.length * distanceRatio)) + i1;
            if (i2 >= target.lines.length){
                i2 = target.lines.length - 1;
            }
        }
        swap(target, i1, i2);
        if(random.nextBoolean()){
            target.reverse[i2] = !target.reverse[i2];
        }
    }

    private static void swap(LineImage lineImage, int index1, int index2)
    {
        if(index1 == index2){
            if(index1 < lineImage.lines.length - 1){
                index2 = index1 + 1;
            }
        }
        Line temp = lineImage.lines[index1];
        lineImage.lines[index1] = lineImage.lines[index2];
        lineImage.lines[index2] = temp;

        boolean reverse =  lineImage.reverse[index1];
        lineImage.reverse[index1] = lineImage.reverse[index2];
        lineImage.reverse[index2] = reverse;
    }
}
