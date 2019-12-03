package org.ao.robopaint.merge;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.norm.NormedLineImage;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ContinuousAreaImageMergerTest {
    private Line line1 = new Line(0, 0, 0, 0);
    private Line line2 = new Line(1, 0, 0, 0);
    private Line line3 = new Line(2, 0, 0, 0);
    private Line line4 = new Line(3, 0, 0, 0);
    private ContinuousAreaImageMerger imageMerger = new ContinuousAreaImageMerger(0.5, null);

    @Test
    public void testMerge3() {
        NormedLineImage target = new NormedLineImage(3);
        NormedLineImage source1 = new NormedLineImage(line1, line2, line3);
        source1.reverse[0] = true;
        NormedLineImage source2 = new NormedLineImage(line1, line3, line2);
        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 0, 1);
        assertArrayEquals(new Line[]{line3, line1, line2}, target.lines);
        assertArrayEquals(new boolean[]{false, true, false}, target.reverse);
    }

    @Test
    public void testMerge4() {
        NormedLineImage target = new NormedLineImage(4);
        NormedLineImage source1 = new NormedLineImage(line1, line2, line3, line4);
        source1.reverse[1] = true;
        NormedLineImage source2 = new NormedLineImage(line3, line4, line1, line2);
        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 2, 0);
        assertArrayEquals(new Line[]{line3, line4, line1, line2}, target.lines);
        assertArrayEquals(new boolean[]{false, false, false, false}, target.reverse);
    }
}