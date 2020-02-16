package org.ao.robopaint.norm;

import org.ao.robopaint.image.Point;
import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.image.indexed.PointIndex;

public class SpeedNormCalculator implements NormCalculator {
    private static final double WHITESPACE_TO_LINE_GAP = 5;

    private final PointIndex pointIndex;
    private final Point start;
    private final double[] norms;
    private final double[] startNorms;

    public SpeedNormCalculator(PointIndex pointIndex, Point start) {
        this.pointIndex = pointIndex;
        this.start = start;
        this.norms = preCalculateNormsForAllPossiblePointCombinations();
        this.startNorms = preCalculateStartNormsForAllPossiblePointCombinations();
    }

    private double[] preCalculateNormsForAllPossiblePointCombinations(){
        int pointCount = pointIndex.getPointCount();
        double []result = new double[pointCount * pointCount];
        for (int start = 0; start < pointCount; start++){
            for (int end = start + 1; end < pointCount; end++){
                result[start * pointCount + end] = result[end * pointCount + start] = calculateNormInternally(pointIndex, start, end);
            }
        }
        return result;
    }

    private double[] preCalculateStartNormsForAllPossiblePointCombinations(){
        int pointCount = pointIndex.getPointCount();
        double []result = new double[pointCount];
        int x1 = start.x;
        int y1 = start.y;

        for (int end = 1; end < pointCount; end++) {
            int x2 = pointIndex.x(end);
            int y2 = pointIndex.y(end);
            result[end] = calculateNorm(x1, y1, x2, y2);
        }
        return result;
    }

    @Override
    public double calculate(IndexedLineImage lineImage) {
        double result = startNorms[lineImage.getStart(0)];
        int pointCount = pointIndex.getPointCount();
        for (int i = 1; i < lineImage.getLineCount(); i++) {
            result += norms[lineImage.getEnd(i - 1) * pointCount + lineImage.getStart(i)];
        }
        return result;
    }

    private static double calculateNorm(int x1, int y1, int x2, int y2){
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public double calculateNorm(int start, int end){
        return calculateNormInternally(pointIndex, start, end);
    }

    private double calculateNormInternally(PointIndex pointIndex, int start, int end){
        int x1 = pointIndex.x(start);
        int y1 = pointIndex.y(start);
        int x2 = pointIndex.x(end);
        int y2 = pointIndex.y(end);

        return calculateNorm(x1, y1, x2, y2);
    }
}
