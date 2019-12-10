package org.ao.robopaint.merge;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.norm.NormedLineImage;
import org.ao.robopaint.norm.SpeedNormCalculator;
import org.ao.robopaint.transform.SwapLineImageTransformerStrategy;
import org.junit.Test;

import static org.junit.Assert.*;

public class FastContinuousAreaImageMergerTest {

    private Line line1 = new Line(0, 0, 0, 0);
    private Line line2 = new Line(1, 0, 0, 0);
    private Line line3 = new Line(2, 0, 0, 0);
    private Line line4 = new Line(3, 0, 0, 0);
    private SpeedNormCalculator normCalculator = new SpeedNormCalculator();
    private FastContinuousAreaImageMerger imageMerger = new FastContinuousAreaImageMerger(0.5, new SwapLineImageTransformerStrategy(normCalculator));

    @Test
    public void testMergeInternally3() {
        NormedLineImage target = new NormedLineImage(3);
        NormedLineImage source1 = new NormedLineImage(line1, line2, line3);
        source1.reverse[0] = true;
        source1.setNorm(normCalculator.calculate(source1));

        NormedLineImage source2 = new NormedLineImage(line1, line3, line2);
        source2.setNorm(normCalculator.calculate(source2));
        int[] indexMapping = new int[source2.lines.length];
        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 0, 1);
        assertArrayEquals(new Line[]{line3, line1, line2}, target.lines);
        assertArrayEquals(new boolean[]{false, true, false}, target.reverse);
        assertArrayEquals(new int[]{1, 0, 2}, indexMapping);
        assertEquals(normCalculator.calculate(target), imageMerger.calculateNorm(source2, target), 0.0001);
    }

    @Test
    public void testMerge4NoOrderChange() {
        NormedLineImage target = new NormedLineImage(4);
        NormedLineImage source1 = new NormedLineImage(line1, line2, line3, line4);
        source1.reverse[1] = true;
        NormedLineImage source2 = new NormedLineImage(line3, line4, line1, line2);
        int[] indexMapping = new int[source2.lines.length];
        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 2, 0);
        assertArrayEquals(new Line[]{line3, line4, line1, line2}, target.lines);
        assertArrayEquals(new boolean[]{false, false, false, false}, target.reverse);
        assertArrayEquals(new int[]{0, 1, 2, 3}, indexMapping);
    }

    @Test
    public void testMerge4() {
        NormedLineImage target = new NormedLineImage(4);
        NormedLineImage source1 = new NormedLineImage(line1, line2, line3, line4);
        source1.reverse[1] = true;
        NormedLineImage source2 = new NormedLineImage(line3, line4, line1, line2);
        int[] indexMapping = new int[source2.lines.length];
        imageMerger.mergeInternally(
                source1,
                source2,
                target,
                2, 2, 2);
        assertArrayEquals(new Line[]{line1, line2, line3, line4}, target.lines);
        assertArrayEquals(new boolean[]{false, false, false, false}, target.reverse);
        assertArrayEquals(new int[]{2, 3, 0, 1}, indexMapping);
    }

}