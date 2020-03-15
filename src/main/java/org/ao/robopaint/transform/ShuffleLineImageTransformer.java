package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.norm.NormCalculator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ShuffleLineImageTransformer implements LineImageTransformer {
    private final double shuffleRatio;
    private final NormCalculator normCalculator;

    public ShuffleLineImageTransformer(double shuffleRatio, NormCalculator normCalculator) {
        this.shuffleRatio = shuffleRatio;
        this.normCalculator = normCalculator;
    }

    public ShuffleLineImageTransformer(NormCalculator normCalculator) {
        shuffleRatio = 1;
        this.normCalculator = normCalculator;
    }

    @Override
    public LineImage transform(LineImage source) {
        LineImage target = new LineImage(source);

        if(shuffleRatio == 1) {
            shuffle(target.lines, target.reverse, 0, target.lines.length - 1);
        }
        else {
            Random random = ThreadLocalRandom.current();
            int shuffleBatchSize = Math.max((int) (target.lines.length * random.nextDouble() * shuffleRatio), 1);
            int start = random.nextInt(target.lines.length - shuffleBatchSize);
            shuffle(target.lines, target.reverse, start, start + shuffleBatchSize - 1);
        }
        target.setNorm(normCalculator.calculate(target));
        return target;
    }

    // Implementing Fisher–Yates shuffle
    private static <T> void shuffle(T[] array1, boolean[] array2, int startIndex, int endIndex)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random random = ThreadLocalRandom.current();
        for (int i = endIndex; i >= startIndex; i--) {
            int index = random.nextInt(i - startIndex + 1) + startIndex;
            // Simple swap
            T a = array1[index];
            array1[index] = array1[i];
            array1[i] = a;

            array2[i] = random.nextBoolean();
        }
    }
}
