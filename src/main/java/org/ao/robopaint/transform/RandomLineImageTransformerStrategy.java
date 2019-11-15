package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLineImageTransformerStrategy implements LineImageTransformerStrategy {
    @Override
    public void transform(LineImage source, LineImage target) {
        target.copyLinesFrom(source);
        shuffle(target.lines);
    }

    // Implementing Fisher–Yates shuffle
    private static <T> void shuffle(T[] array)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random random = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            // Simple swap
            T a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }
}
