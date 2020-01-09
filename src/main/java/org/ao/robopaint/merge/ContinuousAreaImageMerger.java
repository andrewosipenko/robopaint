package org.ao.robopaint.merge;

import org.ao.robopaint.image.indexed.IndexedLine;
import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.norm.NormCalculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
        int pointCount = source1.getPointIndex().getPointCount();
        boolean[][] mergedLines = new boolean[pointCount][pointCount];
        boolean[] targetDefined = new boolean[pointCount];

        copy(source1, target, areaSize, start1, start2, mergedLines, targetDefined);

        List<IndexedLine> skippedLines = getSkippedLines(source2, areaSize, start2, mergedLines);

        fill(source2, target, skippedLines, mergedLines, targetDefined);
    }

    private void copy(IndexedLineImage source1, IndexedLineImage target, int areaSize, int start1, int start2,
                      boolean[][] mergedLines, boolean[] targetDefined) {
        for(int i = 0; i < areaSize; i++){
            int start = source1.getStart(start1 + i);
            int end = source1.getEnd(start1 + i);
            target.set(start2 + i, start, end);
            mergedLines[start][end] = true;
            targetDefined[start2 + i] = true;
        }
    }

    private List<IndexedLine> getSkippedLines(IndexedLineImage source2, int areaSize, int start2, boolean[][] mergedLines){
        List<IndexedLine> skippedLines = new ArrayList<>();
        for(int i = 0; i < areaSize; i++){
            int start = source2.getStart(start2 + i);
            int end = source2.getEnd(start2 + i);

            if(!mergedLines[start][end] && !mergedLines[end][start]){
                skippedLines.add(new IndexedLine(start, end));
            }
        }
        return skippedLines;
    }
    private void fill(IndexedLineImage source2, IndexedLineImage target, List<IndexedLine> skippedLines,
                      boolean[][] mergedLines, boolean[] targetDefined){
        Iterator<IndexedLine> lineIterator = skippedLines.iterator();
        for(int i = 0; i < target.getLineCount(); i++) {
            if(!targetDefined[i]) {
                int start = source2.getStart(i);
                int end = source2.getEnd(i);
                if (mergedLines[start][end] || mergedLines[end][start]){
                    IndexedLine indexedLine = lineIterator.next();
                    target.set(i, indexedLine.getStart(), indexedLine.getEnd());
                }
                else {
                    target.set(i, start, end);
                }
            }
        }
        if(lineIterator.hasNext()) {
            throw new IllegalArgumentException("One line was not processed still");
        }

    }
}
