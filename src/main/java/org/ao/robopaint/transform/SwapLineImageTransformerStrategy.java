package org.ao.robopaint.transform;

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
        target.copyLinesFrom(source);

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
        swap(target.lines, i1, i2);
    }

    // Implementing Fisher–Yates shuffle
    private static <T> void swap(T[] array, int index1, int index2)
    {
        if(index1 == index2){
            if(index1 < array.length - 1){
                index2 = index1 + 1;
            }
        }
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
}
