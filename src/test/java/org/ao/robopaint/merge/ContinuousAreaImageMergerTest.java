package org.ao.robopaint.merge;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContinuousAreaImageMergerTest {
    Line line1 = new Line(0, 0, 1, 0);
    Line line2 = new Line(1, 0, 2, 0);
    Line line3 = new Line(2, 0, 3, 0);
    Line line4 = new Line(3, 0, 4, 0);

    private ContinuousAreaImageMerger imageMerger = new ContinuousAreaImageMerger(0.5, null);

    @Test
    public void testMerge3() {
        LineImage source1 = new LineImage(line1, line2, line3);

        LineImage source2 = new LineImage(line1, line3, line2);

        LineImage target = new LineImage(3);

        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 0, 1);

        assertEquals(line3, target.lines[0]);
        assertFalse(target.reverse[0]);

        assertEquals(line1, target.lines[1]);
        assertFalse(target.reverse[1]);

        assertEquals(line2, target.lines[2]);
        assertFalse(target.reverse[2]);
    }

    @Test
    public void testMerge3reverse() {
        LineImage source1 = new LineImage(line1, line2, line3);
        source1.reverse[0] = true;

        LineImage source2 = new LineImage(line1, line3, line2);

        LineImage target = new LineImage(3);

        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 0, 1);

        assertEquals(line3, target.lines[0]);
        assertFalse(target.reverse[0]);

        assertEquals(line1, target.lines[1]);
        assertTrue(target.reverse[1]);

        assertEquals(line2, target.lines[2]);
        assertFalse(target.reverse[2]);
    }

    @Test
    public void testMerge4() {
        LineImage source1 = new LineImage(line1, line2, line3, line4);
        LineImage source2 = new LineImage(line3, line4, line1, line2);

        LineImage target = new LineImage(4);

        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 2, 0);

        assertEquals(line3, target.lines[0]);

        assertEquals(line4, target.lines[1]);

        assertEquals(line1, target.lines[2]);

        assertEquals(line2, target.lines[3]);
    }

    @Test
    public void testMerge4reverse() {
        LineImage source1 = new LineImage(line1, line2, line3, line4);
        source1.reverse[1] = true;
        LineImage source2 = new LineImage(line3, line4, line1, line2);
        LineImage target = new LineImage(4);

        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 2, 0);

        assertEquals(line3, target.lines[0]);
        assertEquals(line4, target.lines[1]);
        assertEquals(line1, target.lines[2]);
        assertEquals(line2, target.lines[3]);
    }
//
//    @Test
//    public void testMergeBigData() {
//        final int lineCount = 1000;
//        final int times = 100000;
//        PointIndex pointIndex = createSequentialPointIndex(lineCount + 1);
//
//        LineImage source1 = new LineImage(pointIndex, lineCount);
//        for (int i = 0; i < lineCount; i++) {
//            source1.set(i, i, i + 1);
//        }
//
//        LineImage source2 = new LineImage(source1);
//
//        IntStream.range(0, times).forEach(i -> {
//            LineImage target = new LineImage(pointIndex, lineCount);
//            imageMerger.mergeInternally(
//                    source1,
//                    source2,
//                    target,
//                    400, 0, 500);
//
//            assertEquals(400, target.getStart(0));
//            assertEquals(401, target.getEnd(0));
//
//            assertEquals(800, target.getStart(400));
//            assertEquals(801, target.getEnd(400));
//        });
//    }
//
//
//    private PointIndex createSequentialPointIndex(int count){
//        PointIndex pointIndex = new PointIndex(count);
//        for(int i = 0; i < count; i++) {
//            pointIndex.set(i, i, 0);
//        }
//        return pointIndex;
//    }

}