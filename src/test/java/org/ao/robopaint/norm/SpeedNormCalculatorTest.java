package org.ao.robopaint.norm;

import org.ao.robopaint.image.Point;
import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.image.indexed.PointIndex;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpeedNormCalculatorTest {
    private static final int LINE_COUNT = 1000;
    private static final int POINT_COUNT = LINE_COUNT + 1;
    private PointIndex pointIndex;
    private SpeedNormCalculator normCalculator;

    @Before
    public void setUp() {
        pointIndex = createSequentialPointIndex(POINT_COUNT);
        normCalculator = new SpeedNormCalculator(pointIndex, new Point(0, 0));
    }

    @Test
    public void calculate() {
        IndexedLineImage image = new IndexedLineImage(pointIndex, LINE_COUNT);
//        for (int i = 0; i < LINE_COUNT; i++) {
//            image.set(i, i, i + 1);
//        }

        for (int i = 0; i < LINE_COUNT / 2; i++) {
            image.set(i * 2, i * 2, i * 2 + 1);
            image.set(i * 2 + 1, LINE_COUNT - i * 2 - 1, LINE_COUNT - i * 2);
        }

        for(int j = 0; j < 1000000; j++) {
            assertTrue(normCalculator.calculate(image) > 0);
        }
    }

    private PointIndex createSequentialPointIndex(int count){
        PointIndex pointIndex = new PointIndex(count);
        for(int i = 0; i < count; i++) {
            pointIndex.set(i, i, i);
        }
        return pointIndex;
    }
}