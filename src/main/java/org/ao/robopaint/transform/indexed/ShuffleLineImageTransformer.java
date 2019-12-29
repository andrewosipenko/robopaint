package org.ao.robopaint.transform.indexed;

import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.norm.NormCalculator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ShuffleLineImageTransformer extends AbstractAreaTransformer implements LineImageTransformer {

    public ShuffleLineImageTransformer(double shuffleRatio, NormCalculator normCalculator) {
        super(shuffleRatio, 1, normCalculator);
    }

    public ShuffleLineImageTransformer(NormCalculator normCalculator) {
        super(1, 1, normCalculator);
    }

    @Override
    public IndexedLineImage transform(IndexedLineImage source) {
        IndexedLineImage target = new IndexedLineImage(source);

        int lineCount = target.getLineCount();
        if(ratio == 1) {
            shuffle(target, 0, lineCount - 1);
        }
        else {
            int shuffleBatchSize = getArea(source);
            int start = getStart(source, shuffleBatchSize);
            shuffle(target, start, start + shuffleBatchSize - 1);
        }
        target.setNorm(normCalculator.calculate(target));
        return target;
    }

    // Implementing Fisher–Yates shuffle
    private static <T> void shuffle(IndexedLineImage image, int startIndex, int endIndex)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random random = ThreadLocalRandom.current();
        for (int i = endIndex; i >= startIndex; i--) {
            int index = random.nextInt(i - startIndex + 1) + startIndex;
            // Simple swap

            int start = image.getStart(index);
            int end = image.getEnd(index);
            image.set(index, image.getStart(i), image.getEnd(i));
            image.set(i, start, end);
        }
    }
}
