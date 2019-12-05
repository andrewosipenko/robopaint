package org.ao.robopaint.norm;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

public class SpeedNormCalculator implements NormCalculator {
    private static final double WHITESPACE_TO_LINE_GAP = 5;

    @Override
    public double calculate(LineImage lineImage) {
        double result = 0;

        Line[] lines = lineImage.lines;
        if (lines.length == 0) {
            return result;
        }

//        result += calculateNorm(lines[0].x1, lines[0].y1, lines[0].x2, lines[0].y2);
        Line previousLine = lines[0];
        boolean previousReverse = lineImage.reverse[0];
        for (int i = 1; i < lines.length; i++) {
            Line line = lines[i];
            boolean reverse = lineImage.reverse[i];

            result += calculateNorm(previousLine, previousReverse, line, reverse);
            previousLine = line;
            previousReverse = reverse;
        }
        return result;
    }

    private static double calculateNorm(int x1, int y1, int x2, int y2){
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public double calculateNorm(Line previousLine, boolean previousReverse, Line line, boolean reverse){

        final int previousLineEndX;
        final int previousLineEndY;
        if (previousReverse) {
            previousLineEndX = previousLine.x1;
            previousLineEndY = previousLine.y1;
        }
        else {
            previousLineEndX = previousLine.x2;
            previousLineEndY = previousLine.y2;
        }
        final int lineStartX;
        final int lineStartY;
        if (reverse) {
            lineStartX = line.x2;
            lineStartY = line.y2;
        }
        else {
            lineStartX = line.x1;
            lineStartY = line.y1;
        }
        double result = calculateNorm(previousLineEndX, previousLineEndY, lineStartX, lineStartY);
        if (previousLineEndX != lineStartX || previousLineEndY != lineStartY){
            result += WHITESPACE_TO_LINE_GAP;
        }
        return result;
    }
}
