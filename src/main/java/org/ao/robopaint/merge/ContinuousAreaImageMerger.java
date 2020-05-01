package org.ao.robopaint.merge;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.norm.NormCalculator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ContinuousAreaImageMerger implements ImageMerger {
    private final double ratio;
    private NormCalculator normCalculator;

    public ContinuousAreaImageMerger(double ratio, NormCalculator normCalculator) {
        this.ratio = ratio;
        this.normCalculator = normCalculator;
    }

    @Override
    public void merge(LineImage source1, LineImage source2, LineImage target) {
        Random random = ThreadLocalRandom.current();
        int areaSize = Math.max(1, random.nextInt(Math.max(1, (int)(ratio * source1.lines.length))));
        int maxStart = source1.lines.length - areaSize;
        int start1 = random.nextInt(maxStart + 1);
        int start2 = random.nextInt(maxStart + 1);
        mergeInternally(source1, source2, target, areaSize, start1, start2);
        target.setNorm(normCalculator.calculate(target));
    }
    void mergeInternally(LineImage source1, LineImage source2, LineImage target,
                                   int areaSize, int start1, int start2) {
        Set<Line> mergedLines = new HashSet<>(areaSize);
        for(int i = 0; i < areaSize; i++){
            mergedLines.add(target.lines[start2 + i] = source1.lines[start1 + i]);
            target.reverse[start2 + i] = source1.reverse[start1 + i];
        }
        int length = target.lines.length;
        for(int targetIndex = 0, sourceIndex = 0; sourceIndex < length && targetIndex < length; ) {
            if(target.lines[targetIndex] == null) {
                if (!mergedLines.contains(source2.lines[sourceIndex])){
                    target.lines[targetIndex] = source2.lines[sourceIndex];
                    target.reverse[targetIndex] = source2.reverse[sourceIndex];
                    targetIndex++;
                }
                sourceIndex++;
            }
            else {
                targetIndex++;
            }
        }
    }
}
