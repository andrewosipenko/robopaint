package org.ao.robopaint.transform;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.image.Point;
import org.ao.robopaint.norm.SpeedNormCalculator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReverseLineImageTransformerTest {
    Line line1 = new Line(0, 0, 1, 0);
    Line line2 = new Line(1, 0, 2, 0);
    Line line3 = new Line(2, 0, 3, 0);

    private ReverseLineImageTransformer transformer = new ReverseLineImageTransformer(1,
            new SpeedNormCalculator(new Point(0, 0)));
    @Test
    public void shouldTransformFirstOne() {
        LineImage result = transformer.transformInternally(new LineImage(line1, line2, line3), 1, 0);
        assertEquals(line1, result.lines[0]);
        assertTrue(result.reverse[0]);
    }
    @Test
    public void shouldTransformFirstTwo() {
        LineImage result = transformer.transformInternally(new LineImage(line1, line2, line3), 2, 0);
        assertEquals(line2, result.lines[0]);
        assertTrue(result.reverse[0]);
        assertEquals(line1, result.lines[1]);
        assertTrue(result.reverse[1]);
    }
}