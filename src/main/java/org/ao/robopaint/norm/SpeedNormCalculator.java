package org.ao.robopaint.norm;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

public class SpeedNormCalculator implements NormCalculator {
    private static final double WHITESPACE_TO_LINE_GAP = 5;

    @Override
    public double calculate(LineImage lineImage) {
        int result = 0;

        Line[] lines = lineImage.lines;
        if (lines.length == 0) {
            return result;
        }

        result += calculateNorm(lines[0].x1, lines[0].y1, lines[0].x2, lines[0].y2);
        for (int i = 1; i < lines.length; i++) {
            Line previousLine = lines[i - 1];
            Line line = lines[i];

            result += calculateNorm(previousLine.x2, previousLine.y2, line.x1, line.y1);
            if (previousLine.x2 != line.x1 || previousLine.y2 != line.y1){
                result += WHITESPACE_TO_LINE_GAP;
            }
            result += calculateNorm(line.x1, line.y1, line.x2, line.y2);
        }
        return result;
    }

    private double calculateNorm(int x1, int y1, int x2, int y2){
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
