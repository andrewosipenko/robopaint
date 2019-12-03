package org.ao.robopaint.merge;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.norm.NormCalculator;
import org.ao.robopaint.norm.NormedLineImage;

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
    public void merge(NormedLineImage source1, NormedLineImage source2, NormedLineImage target) {
        Random random = ThreadLocalRandom.current();
        int areaSize = Math.max(2, random.nextInt((int)(ratio * source1.lines.length)));
        int maxStart = source1.lines.length - areaSize;
        int start1 = random.nextInt(maxStart);
        int start2 = random.nextInt(maxStart);
        mergeInternally(source1, source2, target, areaSize, start1, start2);
        target.setNorm(normCalculator.calculate(target));
    }
    void mergeInternally(NormedLineImage source1, NormedLineImage source2, NormedLineImage target,
                                   int areaSize, int start1, int start2) {
        Arrays.fill(target.lines, null);
        Set<Line> mergedLines = new HashSet<>(target.lines.length);
        for(int i = start1; i < areaSize; i++){
            mergedLines.add(target.lines[start2 + i] = source1.lines[start1 + i]);
            target.reverse[start2 + i] = source1.reverse[start1 + i];
        }
        List<Line> skippedLines = new ArrayList<>();
        List<Boolean> skippedReverse = new ArrayList<>();
        for(int i = start1; i < areaSize; i++){
            Line line = source2.lines[start2 + i];
            if(!mergedLines.contains(line)){
                skippedLines.add(line);
                skippedReverse.add(source2.reverse[start2 + i]);
            }
        }
        Iterator<Line> lineIterator = skippedLines.iterator();
        Iterator<Boolean> reverseIterator = skippedReverse.iterator();
        for(int i = 0; i < target.lines.length; i++) {
            if(target.lines[i] == null) {
                if (mergedLines.contains(source2.lines[i])){
                    target.lines[i] = lineIterator.next();
                    target.reverse[i] = reverseIterator.next();
                }
                else {
                    target.lines[i] = source2.lines[i];
                    target.reverse[i] = source2.reverse[i];
                }
            }
        }
        if(lineIterator.hasNext()) {
            throw new IllegalArgumentException("One line was not processed still");
        }
    }
}
