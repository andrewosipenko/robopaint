package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ShuffleLineImageTransformerStrategy implements LineImageTransformerStrategy {
    private final double shuffleRatio;

    public ShuffleLineImageTransformerStrategy(double shuffleRatio) {
        this.shuffleRatio = shuffleRatio;
    }

    public ShuffleLineImageTransformerStrategy() {
        shuffleRatio = 1;
    }

    @Override
    public void transform(LineImage source, LineImage target) {
        target.copyLinesFrom(source);

        if(shuffleRatio == 1) {
            shuffle(target.lines, 0, target.lines.length - 1);
        }
        else {
            Random random = ThreadLocalRandom.current();
            int shuffleBatchSize = Math.max((int) (target.lines.length * random.nextDouble() * shuffleRatio), 1);
            int start = random.nextInt(target.lines.length - shuffleBatchSize);
            shuffle(target.lines, start, start + shuffleBatchSize - 1);
        }
    }

    // Implementing Fisher–Yates shuffle
    private static <T> void shuffle(T[] array, int startIndex, int endIndex)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random random = ThreadLocalRandom.current();
        for (int i = endIndex; i >= startIndex; i--) {
            int index = random.nextInt(i - startIndex + 1) + startIndex;
            // Simple swap
            T a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }
}
