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
        for (int i = 1; i < lines.length; i++) {
            Line previousLine = lines[i - 1];
            boolean previousReverse = lineImage.reverse[i - 1];
            Line line = lines[i];
            boolean reverse = lineImage.reverse[i];

            result += calculateNorm(previousLine, previousReverse, line, reverse);
        }
        return result;
    }

    public double calculateNorm(Line previousLine, boolean previousReverse, Line line, boolean reverse){
        double result = 0;

        int previousLineEndX = previousReverse ? previousLine.x1 : previousLine.x2;
        int previousLineEndY = previousReverse ? previousLine.y1 : previousLine.y2;
        int lineStartX = reverse ? line.x2 : line.x1;
        int lineStartY = reverse ? line.y2 : line.y1;

        result += calculateNorm(previousLineEndX, previousLineEndY, lineStartX, lineStartY);
        if (previousLineEndX != lineStartX || previousLineEndY != lineStartY){
            result += WHITESPACE_TO_LINE_GAP;
        }
        return result;
    }

    private static double calculateNorm(int x1, int y1, int x2, int y2){
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
