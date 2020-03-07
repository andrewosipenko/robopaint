package org.ao.robopaint.norm;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SpeedNormCalculatorTest {
    private static final int LINE_COUNT = 1000;
    private static final int POINT_COUNT = LINE_COUNT + 1;
    private Line[] lines;
    private SpeedNormCalculator normCalculator;

    @Before
    public void setUp() {
        lines = createSequentialPointIndex(POINT_COUNT);
        normCalculator = new SpeedNormCalculator();
    }

    @Test
    public void calculate() {
        LineImage image = new LineImage(lines);

        for(int j = 0; j < 1_000_000; j++) {
            assertTrue(normCalculator.calculate(image) > 0);
        }
    }

    private Line[] createSequentialPointIndex(int count){
        List<Line> result = new ArrayList<>(count);
        for (int i = 0; i < count / 2; i++) {
            result.add(new Line(i * 2, i * 2, i * 2 + 1, i * 2 + 1));
            result.add(new Line(count - i * 2 - 1, count - i * 2 - 1, count - i * 2, count - i * 2));
        }
        return result.toArray(new Line[0]);
    }
}