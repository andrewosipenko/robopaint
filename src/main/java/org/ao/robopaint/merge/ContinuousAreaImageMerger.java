package org.ao.robopaint.merge;

import org.ao.robopaint.image.indexed.IndexedLine;
import org.ao.robopaint.image.indexed.IndexedLineImage;
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
    public void merge(IndexedLineImage source1, IndexedLineImage source2, IndexedLineImage target){
        Random random = ThreadLocalRandom.current();
        int areaSize = Math.max(2, random.nextInt((int)(ratio * source1.getLineCount())));
        int maxStart = source1.getLineCount() - areaSize;
        int start1;
        int start2;
        if(maxStart > 0) {
            start1 = random.nextInt(maxStart);
            start2 = random.nextInt(maxStart);
        }
        else {
            start1 = start2 = 0;
        }

        mergeInternally(source1, source2, target, areaSize, start1, start2);
        target.setNorm(normCalculator.calculate(target));
    }
    void mergeInternally(IndexedLineImage source1, IndexedLineImage source2, IndexedLineImage target,
                                   int areaSize, int start1, int start2) {
        int[] mergedStarts = new int[areaSize];
        int[] mergedEnds = new int[areaSize];

        copy(source1, target, areaSize, start1, start2, mergedStarts, mergedEnds);

        fill(source2, target, areaSize, start2, mergedStarts, mergedEnds);
    }

    private void copy(IndexedLineImage source1, IndexedLineImage target, int areaSize, int start1, int start2,
                      int[] mergedStarts, int[] mergedEnds) {
        for(int i = 0; i < areaSize; i++){
            int start = source1.getStart(start1 + i);
            int end = source1.getEnd(start1 + i);
            target.set(start2 + i, start, end);
            if(start >= end) {
                mergedStarts[i] = start;
                mergedEnds[i] = end;
            }
            else {
                mergedStarts[i] = end;
                mergedEnds[i] = start;
            }
        }
    }

    private void fill(IndexedLineImage source2, IndexedLineImage target, int areaSize, int start2,
                      int[] mergedStarts, int[] mergedEnds){
        int skipped = 0;
        for(int targetIndex = 0, source2Index = 0; targetIndex < target.getLineCount(); ) {
            if(targetIndex == start2) {
                targetIndex = start2 + areaSize;
            }
            else {
                int start = source2.getStart(source2Index);
                int end = source2.getEnd(source2Index);
                if (skipped == mergedStarts.length) {
                    target.set(targetIndex, start, end);
                    targetIndex++;
                }
                else {
                    if (contains(mergedStarts, mergedEnds, start, end)) {
                        skipped++;
                    }
                    else {
                        target.set(targetIndex, start, end);
                        targetIndex++;
                    }
                }
                source2Index++;
            }
        }
    }
    private boolean contains(int[] mergedStarts, int[] mergedEnds, int start, int end){
        int start1, end1;
        if(start >= end) {
            start1 = start;
            end1 = end;
        }
        else {
            start1 = end;
            end1 = start;
        }
        for(int i = 0; i < mergedStarts.length; i++){
            if(mergedStarts[i] == start1 && mergedEnds[i] == end1) {
                return true;
            }
        }
        return false;
    }
}
