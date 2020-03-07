package org.ao.robopaint.image;

import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.image.indexed.PointIndex;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImageConverterTest {
    private ImageConverter imageConverter = new ImageConverter();

    @Test
    public void shouldConvert1LineImage() {
        LineImage lineImage = new LineImage(
                new Line(0, 0, 1, 0)
        );

        IndexedLineImage indexedLineImage = imageConverter.convert(lineImage);

        PointIndex pointIndex = indexedLineImage.getPointIndex();
        assertEquals(0, pointIndex.x(0));
        assertEquals(0, pointIndex.y(0));
        assertEquals(1, pointIndex.x(1));
        assertEquals(0, pointIndex.y(1));

        assertEquals(0, indexedLineImage.getStart(0));
        assertEquals(1, indexedLineImage.getEnd(0));
    }

    @Test
    public void shouldConvert2LineImage() {
        LineImage lineImage = new LineImage(
                new Line(0, 0, 1, 0),
                new Line(1, 0, 2, 0)
        );

        IndexedLineImage indexedLineImage = imageConverter.convert(lineImage);

        PointIndex pointIndex = indexedLineImage.getPointIndex();
        assertEquals(0, pointIndex.x(0));
        assertEquals(0, pointIndex.y(0));
        assertEquals(1, pointIndex.x(1));
        assertEquals(0, pointIndex.y(1));
        assertEquals(2, pointIndex.x(2));
        assertEquals(0, pointIndex.y(1));

        assertEquals(0, indexedLineImage.getStart(0));
        assertEquals(1, indexedLineImage.getEnd(0));
        assertEquals(1, indexedLineImage.getStart(1));
        assertEquals(2, indexedLineImage.getEnd(1));
    }
}