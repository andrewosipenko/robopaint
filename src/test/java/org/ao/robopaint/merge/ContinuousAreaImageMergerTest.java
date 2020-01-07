package org.ao.robopaint.merge;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.image.indexed.PointIndex;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContinuousAreaImageMergerTest {
    private Line line1 = new Line(0, 0, 0, 0);
    private Line line2 = new Line(1, 0, 0, 0);
    private Line line3 = new Line(2, 0, 0, 0);
    private Line line4 = new Line(3, 0, 0, 0);
    private ContinuousAreaImageMerger imageMerger = new ContinuousAreaImageMerger(0.5, null);

    @Test
    public void testMerge3() {
        PointIndex pointIndex = createSequentialPointIndex(4);

        IndexedLineImage source1 = new IndexedLineImage(pointIndex, 3);
        source1.set(0, 0, 1);
        source1.set(1, 1, 2);
        source1.set(2, 2, 3);

        IndexedLineImage source2 = new IndexedLineImage(pointIndex, 3);
        source2.set(0, 0, 1);
        source2.set(1, 2, 3);
        source2.set(2, 1, 2);

        IndexedLineImage target = new IndexedLineImage(pointIndex, 3);

        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 0, 1);

        assertEquals(2, target.getStart(0));
        assertEquals(3, target.getEnd(0));

        assertEquals(0, target.getStart(1));
        assertEquals(1, target.getEnd(1));

        assertEquals(1, target.getStart(2));
        assertEquals(2, target.getEnd(2));
    }

    @Test
    public void testMerge3reverse() {
        PointIndex pointIndex = createSequentialPointIndex(4);

        IndexedLineImage source1 = new IndexedLineImage(pointIndex, 3);
        source1.set(0, 1, 0);
        source1.set(1, 1, 2);
        source1.set(2, 2, 3);

        IndexedLineImage source2 = new IndexedLineImage(pointIndex, 3);
        source2.set(0, 0, 1);
        source2.set(1, 2, 3);
        source2.set(2, 1, 2);

        IndexedLineImage target = new IndexedLineImage(pointIndex, 3);

        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 0, 1);

        assertEquals(2, target.getStart(0));
        assertEquals(3, target.getEnd(0));

        assertEquals(1, target.getStart(1));
        assertEquals(0, target.getEnd(1));

        assertEquals(1, target.getStart(2));
        assertEquals(2, target.getEnd(2));
    }

    @Test
    public void testMerge4() {
        PointIndex pointIndex = createSequentialPointIndex(5);

        IndexedLineImage source1 = new IndexedLineImage(pointIndex, 4);
        source1.set(0, 0, 1);
        source1.set(1, 1, 2);
        source1.set(2, 2, 3);
        source1.set(3, 3, 4);

        IndexedLineImage source2 = new IndexedLineImage(pointIndex, 4);
        source2.set(0, 2, 3);
        source2.set(1, 3, 4);
        source2.set(2, 0, 1);
        source2.set(3, 1, 2);

        IndexedLineImage target = new IndexedLineImage(pointIndex, 4);

        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 2, 0);

        assertEquals(2, target.getStart(0));
        assertEquals(3, target.getEnd(0));

        assertEquals(3, target.getStart(1));
        assertEquals(4, target.getEnd(1));

        assertEquals(0, target.getStart(2));
        assertEquals(1, target.getEnd(2));

        assertEquals(1, target.getStart(3));
        assertEquals(2, target.getEnd(3));
    }

    @Test
    public void testMerge4reverse() {
        PointIndex pointIndex = createSequentialPointIndex(5);

        IndexedLineImage source1 = new IndexedLineImage(pointIndex, 4);
        source1.set(0, 0, 1);
        source1.set(1, 2, 1);
        source1.set(2, 2, 3);
        source1.set(3, 3, 4);

        IndexedLineImage source2 = new IndexedLineImage(pointIndex, 4);
        source2.set(0, 2, 3);
        source2.set(1, 3, 4);
        source2.set(2, 0, 1);
        source2.set(3, 1, 2);

        IndexedLineImage target = new IndexedLineImage(pointIndex, 4);

        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 2, 0);

        assertEquals(2, target.getStart(0));
        assertEquals(3, target.getEnd(0));

        assertEquals(3, target.getStart(1));
        assertEquals(4, target.getEnd(1));

        assertEquals(0, target.getStart(2));
        assertEquals(1, target.getEnd(2));

        assertEquals(1, target.getStart(3));
        assertEquals(2, target.getEnd(3));
    }

    private PointIndex createSequentialPointIndex(int count){
        PointIndex pointIndex = new PointIndex(count);
        for(int i = 0; i < count; i++) {
            pointIndex.set(i, i, 0);
        }
        return pointIndex;
    }

}